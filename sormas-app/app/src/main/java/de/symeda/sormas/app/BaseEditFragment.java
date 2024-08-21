/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.app;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.OnRebindCallback;
import androidx.databinding.ViewDataBinding;

import de.symeda.sormas.api.Disease;
import de.symeda.sormas.api.FormType;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.fieldaccess.UiFieldAccessCheckers;
import de.symeda.sormas.api.utils.fieldvisibility.FieldVisibilityCheckers;
import de.symeda.sormas.app.backend.common.AbstractDomainObject;
import de.symeda.sormas.app.backend.common.DatabaseHelper;
import de.symeda.sormas.app.backend.config.ConfigProvider;
import de.symeda.sormas.app.backend.formbuilder.FormBuilder;
import de.symeda.sormas.app.backend.formfield.FormField;
import de.symeda.sormas.app.component.controls.ControlPropertyEditField;
import de.symeda.sormas.app.core.IUpdateSubHeadingTitle;
import de.symeda.sormas.app.core.NotImplementedException;
import de.symeda.sormas.app.core.NotificationContext;
import de.symeda.sormas.app.util.SoftKeyboardHelper;

public abstract class BaseEditFragment<TBinding extends ViewDataBinding, TData, TActivityRootData extends AbstractDomainObject> extends BaseFragment {

	public static final String TAG = BaseEditFragment.class.getSimpleName();

//    private AsyncTask jobTask;
	private BaseEditActivity baseEditActivity;
	private IUpdateSubHeadingTitle subHeadingHandler;
	private NotificationContext notificationCommunicator;

	private TBinding contentViewStubBinding;
	private View contentViewStubRoot;
	private ViewDataBinding rootBinding;
	private View rootView;

	private boolean skipAfterLayoutBinding = false;
	private TActivityRootData activityRootData;
	private boolean liveValidationDisabled;

	private UserRight editUserRight;

	protected static <TFragment extends BaseEditFragment> TFragment newInstance(
		Class<TFragment> fragmentClass,
		Bundle data,
		AbstractDomainObject activityRootData) {
		return newInstance(fragmentClass, data, activityRootData, null);
	}

	protected static <TFragment extends BaseEditFragment> TFragment newInstance(
		Class<TFragment> fragmentClass,
		Bundle data,
		AbstractDomainObject activityRootData,
		UserRight editUserRight) {
		TFragment fragment = newInstance(fragmentClass, data);
		fragment.setActivityRootData(activityRootData);
		((BaseEditFragment) fragment).editUserRight = editUserRight;
		return fragment;
	}

	protected static <TFragment extends BaseEditFragment> TFragment newInstanceWithFieldCheckers(
		Class<TFragment> fragmentClass,
		Bundle data,
		AbstractDomainObject activityRootData,
		FieldVisibilityCheckers fieldVisibilityCheckers,
		UiFieldAccessCheckers fieldAccessCheckers) {
		return newInstanceWithFieldCheckers(fragmentClass, data, activityRootData, fieldVisibilityCheckers, fieldAccessCheckers, null);
	}

	protected static <TFragment extends BaseEditFragment> TFragment newInstanceWithFieldCheckers(
		Class<TFragment> fragmentClass,
		Bundle data,
		AbstractDomainObject activityRootData,
		FieldVisibilityCheckers fieldVisibilityCheckers,
		UiFieldAccessCheckers fieldAccessCheckers,
		UserRight editUserRight) {
		TFragment fragment = newInstance(fragmentClass, data, activityRootData, editUserRight);
		fragment.setFieldVisibilityCheckers(fieldVisibilityCheckers);
		fragment.setFieldAccessCheckers(fieldAccessCheckers);

		((BaseEditFragment) fragment).editUserRight = editUserRight;

		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onPause() {
		super.onPause();

		SoftKeyboardHelper.hideKeyboard(getActivity(), this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
		if (getActivity() instanceof BaseEditActivity) {
			this.baseEditActivity = (BaseEditActivity) this.getActivity();
		} else {
			throw new NotImplementedException("The edit activity for fragment must implement BaseEditActivity");
		}

		if (getActivity() instanceof IUpdateSubHeadingTitle) {
			this.subHeadingHandler = (IUpdateSubHeadingTitle) this.getActivity();
		} else {
			throw new NotImplementedException("Activity for fragment does not support updateSubHeadingTitle; " + "implement IUpdateSubHeadingTitle");
		}

		if (getActivity() instanceof NotificationContext) {
			this.notificationCommunicator = (NotificationContext) this.getActivity();
		} else {
			throw new NotImplementedException("Activity for fragment does not support showErrorNotification; " + "implement NotificationContext");
		}

		super.onCreateView(inflater, container, savedInstanceState);

		//Inflate Root
		rootBinding = DataBindingUtil.inflate(inflater, getRootEditLayout(), container, false);
		rootView = rootBinding.getRoot();

		if (getActivityRootData() == null) {
			// may happen when android tries to re-create old fragments for an activity
			return rootView;
		}

		final ViewStub vsChildFragmentFrame = (ViewStub) rootView.findViewById(R.id.vsChildFragmentFrame);
		vsChildFragmentFrame.setOnInflateListener(new ViewStub.OnInflateListener() {

			@Override
			public void onInflate(ViewStub stub, View inflated) {

				contentViewStubBinding = DataBindingUtil.bind(inflated);
				contentViewStubBinding.addOnRebindCallback(new OnRebindCallback() {

					@Override
					public void onBound(ViewDataBinding binding) {
						super.onBound(binding);

						if (!skipAfterLayoutBinding)
							onAfterLayoutBinding(contentViewStubBinding);
						skipAfterLayoutBinding = false;

						getSubHeadingHandler().updateSubHeadingTitle(getSubHeadingTitle());
					}
				});
				onLayoutBinding(contentViewStubBinding);
				applyLiveValidationDisabledToChildren();
				contentViewStubRoot = contentViewStubBinding.getRoot();

				if (makeHeightMatchParent()) {
					contentViewStubRoot.getLayoutParams().height = MATCH_PARENT;
				} else {
					contentViewStubRoot.getLayoutParams().height = WRAP_CONTENT;
				}

				ViewGroup root = (ViewGroup) getContentBinding().getRoot();
				setNotificationContextForPropertyFields(root);
			}
		});

		vsChildFragmentFrame.setLayoutResource(getEditLayout());
		prepareFragmentData();
		vsChildFragmentFrame.inflate();

		getBaseEditActivity().processActionbarMenu();

		return rootView;
	}

	protected void updateEmptyListHint(List list) {
		if (rootView == null)
			return;
		TextView emptyListHintView = (TextView) rootView.findViewById(R.id.emptyListHint);
		if (emptyListHintView == null)
			return;

		if (list == null || list.isEmpty()) {
			emptyListHintView
				.setText(getResources().getString(isShowNewAction() ? R.string.hint_no_records_found_add_new : R.string.hint_no_records_found));
			emptyListHintView.setVisibility(View.VISIBLE);
		} else {
			emptyListHintView.setVisibility(View.GONE);
		}
	}

	public void requestLayoutRebind() {
		if (contentViewStubBinding != null) {
			onLayoutBinding(contentViewStubBinding);
			applyLiveValidationDisabledToChildren();
		}
	}

	public int getRootEditLayout() {
		return R.layout.fragment_root_edit_layout;
	}

	public abstract int getEditLayout();

	public boolean makeHeightMatchParent() {
		return false;
	}

	public IUpdateSubHeadingTitle getSubHeadingHandler() {
		return this.subHeadingHandler;
	}

	public BaseEditActivity getBaseEditActivity() {
		return this.baseEditActivity;
	}

	protected String getSubHeadingTitle() {
		return null;
	}

	public void setActivityRootData(TActivityRootData activityRootData) {
		this.activityRootData = activityRootData;
	}

	protected TActivityRootData getActivityRootData() {
		return this.activityRootData;
	}

	public abstract TData getPrimaryData();

	public ViewDataBinding getRootBinding() {
		return rootBinding;
	}

	public TBinding getContentBinding() {
		return contentViewStubBinding;
	}

	protected abstract void prepareFragmentData();

	protected abstract void onLayoutBinding(TBinding contentBinding);

	protected void onAfterLayoutBinding(TBinding contentBinding) {
		if (editUserRight != null && !ConfigProvider.hasUserRight(editUserRight)) {
			ViewGroup root = (ViewGroup) getContentBinding().getRoot();
			disableAllEditFields(root);
		}
	}

	public void setLiveValidationDisabled(boolean liveValidationDisabled) {
		if (this.liveValidationDisabled != liveValidationDisabled) {
			this.liveValidationDisabled = liveValidationDisabled;
			applyLiveValidationDisabledToChildren();
		}
	}

	public boolean isLiveValidationDisabled() {
		return liveValidationDisabled;
	}

	public void applyLiveValidationDisabledToChildren() {
		if (getContentBinding() == null)
			return;
		ViewGroup root = (ViewGroup) getContentBinding().getRoot();
		ControlPropertyEditField.applyLiveValidationDisabledToChildren(root, isLiveValidationDisabled());
	}

	public void setNotificationContextForPropertyFields(ViewGroup parent) {
		for (int i = 0; i < parent.getChildCount(); i++) {
			View child = parent.getChildAt(i);
			if (child instanceof ControlPropertyEditField) {
				((ControlPropertyEditField) child).setNotificationContext(notificationCommunicator);
			} else if (child instanceof ViewGroup) {
				setNotificationContextForPropertyFields((ViewGroup) child);
			}
		}
	}

	public boolean isShowSaveAction() {
		if (editUserRight != null) {
			return ConfigProvider.hasUserRight(editUserRight);
		}
		return true;
	}

	public boolean isShowNewAction() {
		return false;
	}

	protected void disableAllEditFields(ViewGroup parent) {
		for (int i = 0; i < parent.getChildCount(); i++) {
			View child = parent.getChildAt(i);
			if (child instanceof ControlPropertyEditField) {
				child.setEnabled(false);
			} else if (child instanceof ViewGroup) {
				disableAllEditFields((ViewGroup) child);
			}
		}
	}

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        if (jobTask != null && !jobTask.isCancelled())
//            jobTask.cancel(true);
//    }

	public void hideFieldsForDisease(Disease diseaseName, LinearLayout mainContent, FormType formType) {
		// Get the relevant fields for the given disease
		List<String> relevantFields = getFieldsForDisease(diseaseName, formType);
		boolean hasVisibleChild = false;
		if (relevantFields.isEmpty()) {
			for (int i = 0; i < mainContent.getChildCount(); i++) {
				View child = mainContent.getChildAt(i);
				if (child instanceof ControlPropertyEditField) {
					child.setVisibility(View.VISIBLE);
				}
			}
			return;
		}

		// Loop through the children of the mainContent and hide fields not in the relevantFields list
		for (int i = 0; i < mainContent.getChildCount(); i++) {
			View child = mainContent.getChildAt(i);
			if (child instanceof ControlPropertyEditField) {
				String childIdName = getResources().getResourceEntryName(child.getId());
				if (!relevantFields.contains(childIdName)) {
					child.setVisibility(View.GONE);
				} else {
					child.setVisibility(View.VISIBLE); // Ensure relevant fields are visible
				}
			} else if (child instanceof LinearLayout) {
				LinearLayout childLayout = (LinearLayout) child;
				boolean layoutHasVisibleField = false;

				// Loop through the children of the LinearLayout
				for (int j = 0; j < childLayout.getChildCount(); j++) {
					View grandChild = childLayout.getChildAt(j);

					if (grandChild instanceof ControlPropertyEditField) {
						String childIdName = getResources().getResourceEntryName(grandChild.getId());

						if (relevantFields.isEmpty() || relevantFields.contains(childIdName)) {
							grandChild.setVisibility(View.VISIBLE);
							layoutHasVisibleField = true;
						} else {
							grandChild.setVisibility(View.GONE);
						}
					}
				}

				if (!layoutHasVisibleField) {
					childLayout.setVisibility(View.GONE);
				} else {
					childLayout.setVisibility(View.VISIBLE);
					hasVisibleChild = true;
				}
			} else if (child instanceof  TextView) {
				String childIdName = getResources().getResourceEntryName(child.getId());

				// Hide the TextView if it's not in the relevantFields list
				if (relevantFields.isEmpty() || relevantFields.contains(childIdName)) {
					child.setVisibility(View.VISIBLE);
					hasVisibleChild = true;
				} else {
					child.setVisibility(View.GONE);
				}
			}
		}

	}


	public List<String> getFieldsForDisease(Disease diseaseName, FormType formType) {

		FormBuilder formBuilder = DatabaseHelper.getFormBuilderDao().getFormBuilder(formType, diseaseName);
		if (formBuilder != null) {
			List<FormField> formFields = DatabaseHelper.getFormBuilderDao().getFormBuilderFormFields(formBuilder);
			return formFields.stream().map(FormField::getFieldName).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}
}
