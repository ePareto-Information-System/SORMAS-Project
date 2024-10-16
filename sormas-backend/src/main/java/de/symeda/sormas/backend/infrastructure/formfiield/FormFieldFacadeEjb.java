package de.symeda.sormas.backend.infrastructure.formfiield;

import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.i18n.Strings;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.infrastructure.facility.FacilityHelper;
import de.symeda.sormas.api.infrastructure.fields.*;
import de.symeda.sormas.api.infrastructure.forms.*;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;
import de.symeda.sormas.backend.feature.FeatureConfigurationFacadeEjb;
import de.symeda.sormas.backend.infrastructure.AbstractInfrastructureFacadeEjb;
import de.symeda.sormas.backend.util.DtoHelper;
import de.symeda.sormas.backend.util.QueryHelper;
import de.symeda.sormas.backend.util.RightsAllowed;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless(name = "FormFieldFacade")
@RightsAllowed(UserRight._INFRASTRUCTURE_VIEW)
public class FormFieldFacadeEjb extends AbstractInfrastructureFacadeEjb<FormField, FormFieldsDto, FormFieldIndexDto, FormFieldReferenceDto, FormFieldService, FormFieldsCriteria> implements FormFieldFacade {

    public FormFieldFacadeEjb() {
    }

    @Inject
    protected FormFieldFacadeEjb(FormFieldService service, FeatureConfigurationFacadeEjb.FeatureConfigurationFacadeEjbLocal featureConfiguration) {
        super(
                FormField.class,
                FormFieldsDto.class,
                service,
                featureConfiguration,
                Validations.importFacilityAlreadyExists,
                null,
                Strings.messageDiseaseDeachivingOrAchivingNotPossible);
    }

    @Override
    public List<FormFieldIndexDto> getIndexList(FormFieldsCriteria criteria, Integer first, Integer max, List<SortProperty> sortProperties) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<FormFieldIndexDto> cq = cb.createQuery(FormFieldIndexDto.class);
        Root<FormField> formBuilderRoot = cq.from(FormField.class);

        if (criteria != null) {
            Predicate filter = service.buildCriteriaFilter(criteria, cb, formBuilderRoot);
            if (filter != null) {
                filter = CriteriaBuilderHelper.and(cb, filter);
                cq.where(filter);
            }
        }

//        if (CollectionUtils.isNotEmpty(sortProperties)) {
//            List<Order> order = new ArrayList<>(sortProperties.size());
//            for (SortProperty sortProperty : sortProperties) {
//                Expression<?> expression;
//                switch (sortProperty.propertyName) {
//                    case FormField.UUID:
//                    case FormField.FORM_TYPE:
//                        expression = formBuilderRoot.get(sortProperty.propertyName);
//                        break;
//                    default:
//                        throw new IllegalArgumentException(sortProperty.propertyName);
//                }
//                order.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
//            }
//            cq.orderBy(order);
//        } else {
//            cq.orderBy(
//                    cb.asc(formBuilderRoot.get(FormField.FORM_TYPE))
//            );
//        }

        cq.multiselect(
                formBuilderRoot.get(FormField.UUID),
                formBuilderRoot.get(FormField.FORM_TYPE),
                formBuilderRoot.get(FormField.FIELD_NAME),
                formBuilderRoot.get(FormField.DESCRIPTION),
                formBuilderRoot.get(FormField.ACTIVE)
        );

        TypedQuery typedQuery = em.createQuery(cq);
        String hqlQueryString=typedQuery.unwrap(org.hibernate.query.Query.class).getQueryString();
        ASTQueryTranslatorFactory queryTranslatorFactory = new ASTQueryTranslatorFactory();
        SessionImplementor hibernateSession = em.unwrap(SessionImplementor.class);
        QueryTranslator queryTranslator = queryTranslatorFactory.createQueryTranslator("", hqlQueryString, java.util.Collections.EMPTY_MAP, hibernateSession.getFactory(), null);
        queryTranslator.compile(java.util.Collections.EMPTY_MAP, false);
        String sqlQueryString = queryTranslator.getSQLString();



        List<FormFieldIndexDto> FormFieldIndexDtoList = QueryHelper.getResultList(em, cq, first, max);
        return FormFieldIndexDtoList;
    }

    @Override
    public List<FormFieldReferenceDto> getReferencesByExternalId(String externalId, boolean includeArchivedEntities) {
        return service.getByExternalId(externalId, includeArchivedEntities)
                .stream()
                .map(FormFieldFacadeEjb::toReferenceDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<FormFieldIndexDto> getIndexPage(FormFieldsCriteria criteria, Integer offset, Integer size, List<SortProperty> sortProperties) {
        List<FormFieldIndexDto> FormFieldIndexDtoList = getIndexList(criteria, offset, size, sortProperties);
        long totalElementCount = count(criteria);
        return new Page<>(FormFieldIndexDtoList, offset, size, totalElementCount);
    }


    @Override
    public FormFieldsDto toDto(FormField source) {

        if (source == null) {
            return null;
        }

        FormFieldsDto target = new FormFieldsDto();
        DtoHelper.fillDto(target, source);
        target.setFormType(source.getFormType());
        target.setFieldName(source.getFieldName());
        target.setDescription(source.getDescription());
        target.setActive(source.getActive());
        return target;
    }

    @Override
    protected FormFieldReferenceDto toRefDto(FormField formFieldConfiguration) {
        return toReferenceDto(formFieldConfiguration);
    }

    @Override
    @RightsAllowed({
            UserRight._INFRASTRUCTURE_CREATE,
            UserRight._INFRASTRUCTURE_EDIT })
    public long count(FormFieldsCriteria criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<FormField> root = cq.from(FormField.class);

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

   /* @Override
    public FormFieldsDto save(FormFieldsDto dto, boolean allowMerge) {
        return super.save(dto, allowMerge);
    }*/
    @Override
    public FormFieldsDto save(FormFieldsDto dto, boolean allowMerge) {
        if (dto.getFieldName() != null && dto.getFieldName().contains(",")) {
            String[] fieldNames = dto.getFieldName().split(",");
            for (String fieldName : fieldNames) {
                FormFieldsDto newDto = new FormFieldsDto();
                newDto.setFieldName(fieldName.trim());
                newDto.setFormType(dto.getFormType());
                newDto.setDescription(dto.getDescription() == null ? "" : dto.getDescription());
                super.save(newDto, allowMerge);
            }
            return null; // Return null or the last saved dto
        } else {
            return super.save(dto, allowMerge);
        }
    }

    @Override
    protected FormField fillOrBuildEntity(FormFieldsDto source, FormField target, boolean checkChangeDate, boolean allowUuidOverwrite) {
        target = DtoHelper.fillOrBuildEntity(source, target, FormField::new, checkChangeDate);
        target.setFormType(source.getFormType());
        target.setFieldName(source.getFieldName());
        target.setDescription(source.getDescription());
        target.setActive(source.getActive());
        return target;
    }

    /*@Override
    protected List<FormField> findDuplicates(FormFieldsDto dto, boolean includeArchived) {
        return service.getByUuids(List.of(dto.getUuid()));
    }*/
    @Override
    protected List<FormField> findDuplicates(FormFieldsDto dto, boolean includeArchived) {
        if (dto.getUuid() != null) {
            return service.getByUuids(List.of(dto.getUuid()));
        } else {
            return new ArrayList<>();
        }
    }


    @LocalBean
    @Stateless
    public static class FormFieldFacadeEjbLocal extends FormFieldFacadeEjb {

        public FormFieldFacadeEjbLocal() {
        }

        @Inject
        protected FormFieldFacadeEjbLocal(FormFieldService service, FeatureConfigurationFacadeEjb.FeatureConfigurationFacadeEjbLocal featureConfiguration) {
            super(service, featureConfiguration);
        }
    }

    public static FormFieldReferenceDto toReferenceDto(FormField entity) {

        if (entity == null) {
            return null;
        }

        return new FormFieldReferenceDto(
                entity.getUuid(),
                entity.getFieldName(),
                null);
    }

    @Override
    public List<FormFieldsDto> getFormFieldsByFormType(FormFieldsCriteria criteria) {
        return service.getFormFieldsByFormType(criteria)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
