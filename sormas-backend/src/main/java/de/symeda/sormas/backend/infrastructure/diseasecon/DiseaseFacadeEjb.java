package de.symeda.sormas.backend.infrastructure.diseasecon;


import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.infrastructure.disease.*;
import de.symeda.sormas.api.infrastructure.facility.FacilityHelper;
import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.backend.common.CriteriaBuilderHelper;
import de.symeda.sormas.backend.disease.DiseaseConfiguration;
import de.symeda.sormas.backend.feature.FeatureConfigurationFacadeEjb;
import de.symeda.sormas.backend.infrastructure.AbstractInfrastructureFacadeEjb;
import de.symeda.sormas.backend.infrastructure.facility.Facility;
import de.symeda.sormas.backend.infrastructure.facility.FacilityFacadeEjb;
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.DtoHelper;
import de.symeda.sormas.backend.util.QueryHelper;
import org.apache.commons.collections.CollectionUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless(name = "DiseaseFacade")
public class DiseaseFacadeEjb extends AbstractInfrastructureFacadeEjb<DiseaseConfiguration, DiseaseDto, DiseaseIndexDto, DiseaseReferenceDto, DiseaseService, DiseaseCriteria> implements DiseaseFacade {

    public DiseaseFacadeEjb(){}

    @Inject
    protected DiseaseFacadeEjb(DiseaseService service, FeatureConfigurationFacadeEjb.FeatureConfigurationFacadeEjbLocal featureConfiguration,
                                UserService userService) {
        super(DiseaseConfiguration.class, DiseaseDto.class, service, featureConfiguration, userService,
                Validations.importFacilityAlreadyExists);
    }

    @Override
    public List<DiseaseIndexDto> getIndexList(DiseaseCriteria diseaseCriteria, Integer first, Integer max, List<SortProperty> sortProperties) {

            CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DiseaseIndexDto> cq = cb.createQuery(DiseaseIndexDto.class);
        Root<DiseaseConfiguration> disease = cq.from(DiseaseConfiguration.class);

        if (diseaseCriteria != null) {
            Predicate filter = service.buildCriteriaFilter(diseaseCriteria, cb, disease);
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

        List<DiseaseIndexDto> diseaseIndexDtoList = QueryHelper.getResultList(em, cq, first, max);
        return diseaseIndexDtoList;
    }

    @Override
    public long count(DiseaseCriteria criteria) {
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
    public DiseaseDto save(DiseaseDto dto, boolean allowMerge) {
        return super.save(dto, allowMerge);
    }

    @Override
    public List<DiseaseReferenceDto> getReferencesByExternalId(String externalId, boolean includeArchivedEntities) {
        return null;
    }

    @Override
    public Page<DiseaseIndexDto> getIndexPage(DiseaseCriteria criteria, Integer offset, Integer size, List<SortProperty> sortProperties) {
        List<DiseaseIndexDto> diseaseIndexDtoList = getIndexList(criteria, offset, size, sortProperties);
        long totalElementCount = count(criteria);
        return new Page<>(diseaseIndexDtoList, offset, size, totalElementCount);
    }

    @Override
    public List<DiseaseReferenceDto> getAllActiveAsReference() {
        return service.getAllActive().stream().map(DiseaseFacadeEjb::toReferenceDto).collect(Collectors.toList());
    }

    @Override
    protected void selectDtoFields(CriteriaQuery<DiseaseDto> cq, Root<DiseaseConfiguration> root) {

    }

    @Override
    protected DiseaseConfiguration fillOrBuildEntity(DiseaseDto source, DiseaseConfiguration target, boolean checkChangeDate) {
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
    public DiseaseDto toDto(DiseaseConfiguration source) {

        if (source == null) {
            return null;
        }

        DiseaseDto target = new DiseaseDto();
        DtoHelper.fillDto(target, source);

        target.setDisease(source.getDisease());
        target.setActive(source.getActive());
        target.setPrimaryDisease(source.getPrimaryDisease());
        target.setCaseBased(source.getCaseBased());
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

    public static DiseaseReferenceDto toReferenceDto(DiseaseConfiguration entity) {

        if (entity == null) {
            return null;
        }

        return new DiseaseReferenceDto(
                entity.getUuid(),
                FacilityHelper.buildFacilityString(entity.getUuid(), entity.getDisease().getName()),
                null);
    }

    @Override
    public DiseaseReferenceDto toRefDto(DiseaseConfiguration disease) {
        return null;
    }

    @Override
    protected List<DiseaseConfiguration> findDuplicates(DiseaseDto dto, boolean includeArchived) {
        return null;
    }

    @Override
    public DiseaseDto getByUuid(String uuid) {
        DiseaseConfiguration diseaseDto = service.getByUuid(uuid);
        return toDto(diseaseDto);
    }

}
