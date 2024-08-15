package de.symeda.sormas.backend.infrastructure.formbuilder;

import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.infrastructure.diseasecon.*;
import de.symeda.sormas.api.infrastructure.facility.FacilityHelper;
import de.symeda.sormas.api.infrastructure.fields.FormFieldsDto;
import de.symeda.sormas.api.infrastructure.forms.*;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;
import de.symeda.sormas.backend.disease.DiseaseConfiguration;
import de.symeda.sormas.backend.feature.FeatureConfigurationFacadeEjb;
import de.symeda.sormas.backend.infrastructure.AbstractInfrastructureFacadeEjb;
import de.symeda.sormas.backend.infrastructure.facility.Facility;
import de.symeda.sormas.backend.infrastructure.facility.FacilityFacadeEjb;
import de.symeda.sormas.backend.infrastructure.formfiield.FormField;
import de.symeda.sormas.backend.infrastructure.formfiield.FormFieldFacadeEjb;
import de.symeda.sormas.backend.util.DtoHelper;
import de.symeda.sormas.backend.util.QueryHelper;
import de.symeda.sormas.backend.util.RightsAllowed;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Stateless(name = "FormBuilderFacade")
@RightsAllowed(UserRight._INFRASTRUCTURE_VIEW)
public class FormBuilderFacadeEjb extends AbstractInfrastructureFacadeEjb<FormBuilder, FormBuilderDto, FormBuilderIndexDto, FormBuilderReferenceDto, FromBuilderService, FormBuilderCriteria> implements FormBuilderFacade {

    public FormBuilderFacadeEjb() {
    }

    @Inject
    protected FormBuilderFacadeEjb(FromBuilderService service, FeatureConfigurationFacadeEjb.FeatureConfigurationFacadeEjbLocal featureConfiguration) {
        super(
                FormBuilder.class,
                FormBuilderDto.class,
                service,
                featureConfiguration,
                Validations.importFacilityAlreadyExists,
                null,
                Strings.messageDiseaseDeachivingOrAchivingNotPossible);
    }

    @Override
    public List<FormBuilderIndexDto> getIndexList(FormBuilderCriteria criteria, Integer first, Integer max, List<SortProperty> sortProperties) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<FormBuilderIndexDto> cq = cb.createQuery(FormBuilderIndexDto.class);
        Root<FormBuilder> formBuilderRoot = cq.from(FormBuilder.class);

        if (criteria != null) {
            Predicate filter = service.buildCriteriaFilter(criteria, cb, formBuilderRoot);
            if (filter != null) {
                filter = CriteriaBuilderHelper.and(cb, filter);
                cq.where(filter);
            }
        }

        if (CollectionUtils.isNotEmpty(sortProperties)) {
            List<Order> order = new ArrayList<>(sortProperties.size());
            for (SortProperty sortProperty : sortProperties) {
                Expression<?> expression;
                switch (sortProperty.propertyName) {
                    case FormBuilder.UUID:
                    case FormBuilder.DISEASE:
                        expression = formBuilderRoot.get(sortProperty.propertyName);
                        break;
                    default:
                        throw new IllegalArgumentException(sortProperty.propertyName);
                }
                order.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
            }
            cq.orderBy(order);
        } else {
            cq.orderBy(
                    cb.asc(formBuilderRoot.get(FormBuilder.DISEASE))
            );
        }

        cq.multiselect(
                formBuilderRoot.get(FormBuilder.UUID),
                formBuilderRoot.get(FormBuilder.DISEASE),
                formBuilderRoot.get(FormBuilder.FORM_TYPE)
        );


        List<FormBuilderIndexDto> FormBuilderIndexDtoList = QueryHelper.getResultList(em, cq, first, max);
        return FormBuilderIndexDtoList;
    }

    @Override
    public List<FormBuilderReferenceDto> getReferencesByExternalId(String externalId, boolean includeArchivedEntities) {
        return service.getByExternalId(externalId, includeArchivedEntities)
                .stream()
                .map(FormBuilderFacadeEjb::toReferenceDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<FormBuilderIndexDto> getIndexPage(FormBuilderCriteria criteria, Integer offset, Integer size, List<SortProperty> sortProperties) {
        List<FormBuilderIndexDto> FormBuilderIndexDtoList = getIndexList(criteria, offset, size, sortProperties);
        long totalElementCount = count(criteria);
        return new Page<>(FormBuilderIndexDtoList, offset, size, totalElementCount);
    }


    @Override
    public FormBuilderDto toDto(FormBuilder source) {

        if (source == null) {
            return null;
        }

        FormBuilderDto target = new FormBuilderDto();
        DtoHelper.fillDto(target, source);

        target.setDisease(source.getDisease());
        target.setFormType(source.getFormType());
        target.setActive(source.getActive());
        if (source.getFormFields() != null) {
            target.setFormFields(source.getFormFields().stream().map(FormFieldFacadeEjb::toReferenceDto).collect(Collectors.toList()));
        }

        return target;
    }

    @Override
    protected FormBuilderReferenceDto toRefDto(FormBuilder diseaseConfiguration) {
        return toReferenceDto(diseaseConfiguration);
    }

    @Override
    @RightsAllowed({
            UserRight._INFRASTRUCTURE_CREATE,
            UserRight._INFRASTRUCTURE_EDIT })
    public long count(FormBuilderCriteria criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<FormBuilder> root = cq.from(FormBuilder.class);

        if (criteria != null) {
            Predicate filter = service.buildCriteriaFilter(criteria, cb, root);
            if (filter != null){
                cq.where(filter);
            }
        }


        cq.select(cb.countDistinct(root));
        long totalCount = em.createQuery(cq).getSingleResult();
        return totalCount;
    }

    @Override
    public FormBuilderDto save(FormBuilderDto dto, boolean allowMerge) {
        return super.save(dto, allowMerge);
    }

    @Override
    protected FormBuilder fillOrBuildEntity(FormBuilderDto source, FormBuilder target, boolean checkChangeDate, boolean allowUuidOverwrite) {
        target = DtoHelper.fillOrBuildEntity(source, target, FormBuilder::new, checkChangeDate);

        target.setDisease(source.getDisease());
        target.setFormType(source.getFormType());
        target.setActive(source.getActive());
       if (source.getFormFields() == null) {
           target.setFormFields(new HashSet<>());
       } else {
           target.setFormFields(source.getFormFields().stream().map(formFieldsReferenceDto -> {
               return mapDtoToEntity(formFieldsReferenceDto.getUuid());
           }).collect(Collectors.toSet()));
       }

        return target;
    }

    private FormField mapDtoToEntity(String uuid) {
        FormField formField = service.getFieldByUuid(uuid);
        return formField;
    }

    @Override
    protected List<FormBuilder> findDuplicates(FormBuilderDto dto, boolean includeArchived) {
        return service.getByUuids(List.of(dto.getUuid()));
    }


    @LocalBean
    @Stateless
    public static class FormBuilderFacadeEjbLocal extends FormBuilderFacadeEjb {

        public FormBuilderFacadeEjbLocal() {
        }

        @Inject
        protected FormBuilderFacadeEjbLocal(FromBuilderService service, FeatureConfigurationFacadeEjb.FeatureConfigurationFacadeEjbLocal featureConfiguration) {
            super(service, featureConfiguration);
        }
    }

    public static FormBuilderReferenceDto toReferenceDto(FormBuilder entity) {

        if (entity == null) {
            return null;
        }

        return new FormBuilderReferenceDto(
                entity.getUuid(),
                FacilityHelper.buildFacilityString(entity.getUuid(), entity.getDisease().getName()),
                null);
    }
}
