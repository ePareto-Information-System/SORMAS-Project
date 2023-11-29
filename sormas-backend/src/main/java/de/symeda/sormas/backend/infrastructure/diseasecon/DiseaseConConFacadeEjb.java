package de.symeda.sormas.backend.infrastructure.diseasecon;


import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.infrastructure.diseasecon.*;
import de.symeda.sormas.api.infrastructure.facility.FacilityHelper;
import de.symeda.sormas.api.user.UserRight;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;
import de.symeda.sormas.backend.disease.DiseaseConfiguration;
import de.symeda.sormas.backend.feature.FeatureConfigurationFacadeEjb.FeatureConfigurationFacadeEjbLocal;
import de.symeda.sormas.backend.infrastructure.AbstractInfrastructureFacadeEjb;
import de.symeda.sormas.backend.infrastructure.facility.Facility;
import de.symeda.sormas.backend.infrastructure.facility.FacilityFacadeEjb;
import de.symeda.sormas.backend.util.DtoHelper;
import de.symeda.sormas.backend.util.QueryHelper;
import de.symeda.sormas.backend.util.RightsAllowed;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless(name = "DiseaseFacade")
@RightsAllowed(UserRight._INFRASTRUCTURE_VIEW)

public class DiseaseConConFacadeEjb extends AbstractInfrastructureFacadeEjb<DiseaseConfiguration, DiseaseConDto, DiseaseConIndexDto, DiseaseConReferenceDto, DiseaseConService, DiseaseConCriteria> implements DiseaseConFacade {
    public DiseaseConConFacadeEjb(){}

    @Inject
    protected DiseaseConConFacadeEjb(DiseaseConService service, FeatureConfigurationFacadeEjbLocal featureConfiguration) {
        super(
                DiseaseConfiguration.class,
                DiseaseConDto.class,
                service,
                featureConfiguration,
                "",
                null,
                "");
    }

    @Override
    public List<DiseaseConIndexDto> getIndexList(DiseaseConCriteria diseaseConCriteria, Integer first, Integer max, List<SortProperty> sortProperties) {

            CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DiseaseConIndexDto> cq = cb.createQuery(DiseaseConIndexDto.class);
        Root<DiseaseConfiguration> disease = cq.from(DiseaseConfiguration.class);

        if (diseaseConCriteria != null) {
            Predicate filter = service.buildCriteriaFilter(diseaseConCriteria, cb, disease);
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
                    case DiseaseConfiguration.UUID:
                    case DiseaseConfiguration.DISEASE:
                    case DiseaseConfiguration.ACTIVE:
                    case DiseaseConfiguration.PRIMARY_DISEASE:
                    case DiseaseConfiguration.CASE_BASED:
                    case DiseaseConfiguration.FOLLOW_UP_ENABLED:
                    case DiseaseConfiguration.FOLLOW_UP_DURATION:
                    case DiseaseConfiguration.EXTENDED_CLASSIFICATION:
                        expression = disease.get(sortProperty.propertyName);
                        break;
                    default:
                        throw new IllegalArgumentException(sortProperty.propertyName);
                }
                order.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
            }
            cq.orderBy(order);
        } else {
            cq.orderBy(
                    cb.asc(disease.get(DiseaseConfiguration.DISEASE))
            );
        }

        cq.multiselect(
                disease.get(DiseaseConfiguration.UUID),
                disease.get(DiseaseConfiguration.DISEASE)
        );

        List<DiseaseConIndexDto> diseaseConIndexDtoList = QueryHelper.getResultList(em, cq, first, max);
        return diseaseConIndexDtoList;
    }

    @Override
    @RightsAllowed({
            UserRight._INFRASTRUCTURE_CREATE,
            UserRight._INFRASTRUCTURE_EDIT })
    public long count(DiseaseConCriteria criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<DiseaseConfiguration> root = cq.from(DiseaseConfiguration.class);

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
    public DiseaseConDto save(DiseaseConDto dto, boolean allowMerge) {
        return super.save(dto, allowMerge);
    }

    @Override
    protected DiseaseConfiguration fillOrBuildEntity(DiseaseConDto source, DiseaseConfiguration target, boolean checkChangeDate, boolean allowUuidOverwrite) {
        target = DtoHelper.fillOrBuildEntity(source, target, DiseaseConfiguration::new, checkChangeDate);

        target.setDisease(source.getDisease());
        if (source.getFacilities() == null) {
            source.setFacilities(new ArrayList<>());
        } else {
            target.setFacilities(source.getFacilities().stream().map(facilityReferenceDto -> {
                return mapDtoToEntity(facilityReferenceDto.getUuid());
            }).collect(Collectors.toSet()));
        }

        return target;
    }

    @Override
    public List<DiseaseConReferenceDto> getReferencesByExternalId(String externalId, boolean includeArchivedEntities) {
        return null;
    }

    @Override
    public Page<DiseaseConIndexDto> getIndexPage(DiseaseConCriteria criteria, Integer offset, Integer size, List<SortProperty> sortProperties) {
        List<DiseaseConIndexDto> diseaseConIndexDtoList = getIndexList(criteria, offset, size, sortProperties);
        long totalElementCount = count(criteria);
        return new Page<>(diseaseConIndexDtoList, offset, size, totalElementCount);
    }

    @Override
    public List<DiseaseConReferenceDto> getAllActiveAsReference() {
        return service.getAllActive().stream().map(DiseaseConConFacadeEjb::toReferenceDto).collect(Collectors.toList());
    }

    @Override
    protected DiseaseConfiguration fillOrBuildEntity(DiseaseConDto source, DiseaseConfiguration target, boolean checkChangeDate) {
        target = DtoHelper.fillOrBuildEntity(source, target, DiseaseConfiguration::new, checkChangeDate);

        target.setDisease(source.getDisease());
        if (source.getFacilities() == null) {
            source.setFacilities(new ArrayList<>());
        } else {
            target.setFacilities(source.getFacilities().stream().map(facilityReferenceDto -> {
                return mapDtoToEntity(facilityReferenceDto.getUuid());
            }).collect(Collectors.toSet()));
        }

        return target;
    }

    private Facility mapDtoToEntity(String dto) {
        Facility facility = service.getFacilityByUuid(dto);
        return facility;
    }

    @Override
    public DiseaseConDto toDto(DiseaseConfiguration source) {

        if (source == null) {
            return null;
        }

        DiseaseConDto target = new DiseaseConDto();
        DtoHelper.fillDto(target, source);

        target.setDisease(source.getDisease());
        target.setActive(source.getActive());
        target.setPrimaryDisease(source.getPrimaryDisease());
        target.setFollowUpEnabled(source.getFollowUpEnabled());
        target.setFollowUpDuration(source.getFollowUpDuration());
        target.setCaseFollowUpDuration(source.getCaseFollowUpDuration());
        target.setEventParticipantFollowUpDuration(source.getEventParticipantFollowUpDuration());
        target.setExtendedClassification(source.getExtendedClassification());
        target.setExtendedClassificationMulti(source.getExtendedClassificationMulti());
        //target.setAgeGroups(source.getAgeGroups());

        if (source.getFacilities() != null) {
            target.setFacilities(source.getFacilities().stream().map(FacilityFacadeEjb::toReferenceDto).collect(Collectors.toList()));
        }
        return target;
    }

    public static DiseaseConReferenceDto toReferenceDto(DiseaseConfiguration entity) {

        if (entity == null) {
            return null;
        }

        return new DiseaseConReferenceDto(
                entity.getUuid(),
                FacilityHelper.buildFacilityString(entity.getUuid(), entity.getDisease().getName()),
                null);
    }

    @Override
    public DiseaseConReferenceDto toRefDto(DiseaseConfiguration disease) {
        return null;
    }

    @Override
    protected List<DiseaseConfiguration> findDuplicates(DiseaseConDto dto, boolean includeArchived) {
        return null;
    }

    @Override
    public DiseaseConDto getByUuid(String uuid) {
        DiseaseConfiguration diseaseDto = service.getByUuid(uuid);
        return toDto(diseaseDto);
    }

    @LocalBean
    @Stateless
    public static class DiseaseConConFacadeEjbLocal extends DiseaseConConFacadeEjb {

        public DiseaseConConFacadeEjbLocal() {
        }

        @Inject
        protected DiseaseConConFacadeEjbLocal(DiseaseConService service, FeatureConfigurationFacadeEjbLocal featureConfiguration) {
            super(service, featureConfiguration);
        }
    }

}
