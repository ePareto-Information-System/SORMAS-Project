package de.symeda.sormas.backend.cadre;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import de.symeda.sormas.api.common.Page;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.i18n.Validations;
import de.symeda.sormas.api.infrastructure.cadre.*;
import de.symeda.sormas.api.infrastructure.country.CountryReferenceDto;
import de.symeda.sormas.api.utils.ValidationRuntimeException;
import de.symeda.sormas.backend.caze.CaseUserFilterCriteria;
import de.symeda.sormas.backend.deletionconfiguration.AbstractCoreEntityFacade;
import de.symeda.sormas.backend.infrastructure.country.Country;
import de.symeda.sormas.backend.infrastructure.country.CountryFacadeEjb;
import de.symeda.sormas.backend.util.ModelConstants;
import de.symeda.sormas.backend.util.Pseudonymizer;
import org.apache.commons.collections.CollectionUtils;

import de.symeda.sormas.api.utils.SortProperty;
import de.symeda.sormas.backend.user.UserService;
import de.symeda.sormas.backend.util.DtoHelper;
import de.symeda.sormas.backend.util.QueryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless(name = "CadreFacade")
public class CadreFacadeEjb extends AbstractCoreEntityFacade<Cadre>	implements CadreFacade {
	private static final int ARCHIVE_BATCH_SIZE = 1000;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext(unitName = ModelConstants.PERSISTENCE_UNIT_NAME)
	private EntityManager em;

	@EJB
	private CadreService cadreService;

	@EJB
	private UserService userService;

	public CadreFacadeEjb() {
		super(Cadre.class);
	}

	@Override
	public List<CadreReferenceDto> getAllActiveAsReference() {

		return cadreService.getAll(Cadre.POSITION, true)
				.stream()
				.map(CadreFacadeEjb::toReferenceDto)
				.sorted(Comparator.comparing(CadreReferenceDto::getCaption))
				.collect(Collectors.toList());
	}

	@Override
	public List<CadreDto> getByUuids(List<String> uuids) {
		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight);
		return cadreService.getByUuids(uuids).stream().map(c -> toDataDto(c)).collect(Collectors.toList());
	}

	public CadreDto getByUuid(String uuid) {
		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight);
		return toDataDto(cadreService.getByUuid(uuid));
	}

	@Override
	public long count(CadreCriteria cadreCriteria) {

		return count(cadreCriteria, false);
	}

	@Override
	public long count(CadreCriteria cadreCriteria, boolean ignoreUserFilter) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Cadre> root = cq.from(Cadre.class);
		Predicate filter = null;

		if (cadreCriteria != null) {
			filter = cadreService.buildCriteriaFilter(cadreCriteria, cb, root);
		}
		if (filter != null) {
			cq.where(filter);
		}

		cq.select(cb.countDistinct(root));
		return em.createQuery(cq).getSingleResult();
	}

	@Override
	public Page<CadreDto> getIndexPage(CadreCriteria cadreCriteria, Integer offset, Integer size, List<SortProperty> sortProperties) {
		List<CadreDto> cadreIndexList = getIndexList(cadreCriteria, offset, size, sortProperties);
		long totalElementCount = count(cadreCriteria);
		return new Page<>(cadreIndexList, offset, size, totalElementCount);
	}

	@Override
	public List<CadreDto> getIndexList(CadreCriteria criteria, Integer first, Integer max, List<SortProperty> sortProperties) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Cadre> cq = cb.createQuery(Cadre.class);
		Root<Cadre> cadreRoot = cq.from(Cadre.class);

		Predicate filter = null;
		if (criteria != null) {
			filter = cadreService.buildCriteriaFilter(criteria, cb, cadreRoot);
		}
		if (filter != null) {
			cq.where(filter);
		}

//		if (CollectionUtils.isNotEmpty(sortProperties)) {
			List<Order> order = new ArrayList<>(sortProperties.size());
			for (SortProperty sortProperty : sortProperties) {
				Expression<?> expression;
				switch (sortProperty.propertyName) {
					case Cadre.POSITION:
					case Cadre.EXTERNAL_ID:
						expression = cadreRoot.get(sortProperty.propertyName);
					break;
					default:
						throw new IllegalArgumentException(sortProperty.propertyName);
				}
				order.add(sortProperty.ascending ? cb.asc(expression) : cb.desc(expression));
			}
			cq.orderBy(order);
//		} else {
//			cq.orderBy(cb.asc(cadreRoot.get(Cadre.POSITION)));
//		}

		cq.select(cadreRoot);

		return QueryHelper.getResultList(em, cq, first, max, this::toDataDto);
	}

	@Override
	public List<CadreReferenceDto> getByPosition(String name, boolean includeArchived) {
		return cadreService.getByPosition(name, includeArchived).stream().map(CadreFacadeEjb::toReferenceDto).collect(Collectors.toList());
	}

	@Override
	public CadreDto saveCadre(@Valid CadreDto dto) throws ValidationRuntimeException {
		return saveCadre(dto, true, true, true, false);
	}

	@Override
	public CadreDto saveCadre(@Valid CadreDto dto, Boolean systemSave) throws ValidationRuntimeException {
		return saveCadre(dto, true, true, true, systemSave);
	}

	public CadreDto saveCadre(@Valid CadreDto dto, boolean handleChanges, boolean checkChangeDate, Boolean systemSave) {
		return saveCadre(dto, handleChanges, checkChangeDate, true, systemSave);
	}

	public CadreDto saveCadre(@Valid CadreDto dto, boolean handleChanges, boolean checkChangeDate, boolean internal, Boolean systemSave)
			throws ValidationRuntimeException {

		Cadre existingCase = cadreService.getByUuid(dto.getUuid());

//		if (!systemSave && internal && existingCase != null && !cadreService.isCaseEditAllowed(existingCase)) {
//			throw new AccessDeniedException(I18nProperties.getString(Strings.errorCaseNotEditable));
//		}

		CadreDto existingCadreDto = handleChanges ? toDto(existingCase) : null;

		return caseSave(dto, handleChanges, existingCase, existingCadreDto, checkChangeDate, internal);
	}

	private CadreDto caseSave(
			@Valid CadreDto dto,
			boolean handleChanges,
			Cadre existingCadre,
			CadreDto existingCadreDto,
			boolean checkChangeDate,
			boolean syncShares)
			throws ValidationRuntimeException {

		if (dto == null || dto.getPosition() == null) {
			throw new ValidationRuntimeException(I18nProperties.getValidationError(Validations.validCadrePosition));
		}

		existingCadre = fillOrBuildEntity(dto, existingCadre, checkChangeDate);

		// Set version number on a new case
//		if (existingCadre.getCreationDate() == null && StringUtils.isEmpty(dto.getCreationVersion())) {
//			existingCadre.setCreationDate(InfoProvider.get().getVersion());
//		}

		doSave(existingCadre, handleChanges, existingCadreDto, syncShares);

		return toDataDto(existingCadre);
	}

	private void doSave(Cadre cadre, boolean handleChanges, CadreDto existingCaseDto, boolean syncShares) {
		cadreService.ensurePersisted(cadre);
//		if (handleChanges) {
//			updateCaseVisitAssociations(existingCaseDto, cadre);
//			caseService.updateFollowUpDetails(cadre, existingCaseDto != null && cadre.getFollowUpStatus() != existingCaseDto.getFollowUpStatus());
//
//			onCaseChanged(existingCaseDto, cadre, syncShares);
//		}
	}

	public Cadre fillOrBuildEntity(@NotNull CadreDto source, Cadre target, boolean checkChangeDate) {
		target = DtoHelper.fillOrBuildEntity(source, target, Cadre::new, checkChangeDate);
		target.setPosition(source.getPosition());
		target.setExternalId(source.getExternalId());
		target.setArchived(source.isArchived());
		return target;
	}

	@Override
	public List<CadreDto> getByExternalId(String externalId) {
//		Pseudonymizer pseudonymizer = Pseudonymizer.getDefault(userService::hasRight);
		return cadreService.getByExternalId(externalId).stream().map(this::toDto).collect(Collectors.toList());
	}

	public Cadre toCadre(CadreDto dto) {
		if (dto == null) {
			return null;
		}
		Cadre cadre = new Cadre();
//		DtoHelper.fillDto(dto, cadre);

		cadre.setUuid(dto.getUuid());
		cadre.setPosition(dto.getPosition());
		cadre.setExternalId(dto.getExternalId());
		cadre.setArchived(dto.isArchived());
		return cadre;
	}

	public CadreDto toDataDto(Cadre entity) {
		if (entity == null) {
			return null;
		}
		CadreIndexDto dto = new CadreIndexDto();
		DtoHelper.fillDto(dto, entity);

		dto.setPosition(entity.getPosition());
		dto.setExternalId(entity.getExternalId());
		dto.setArchived(entity.isArchived());
		return dto;
	}

	public CadreIndexDto toDto(Cadre entity) {
		if (entity == null) {
			return null;
		}
		CadreIndexDto dto = new CadreIndexDto();
		DtoHelper.fillDto(dto, entity);

		dto.setPosition(entity.getPosition());
		dto.setExternalId(entity.getExternalId());
		dto.setArchived(entity.isArchived());
		return dto;
	}

	public CadreReferenceDto toRefDto(Cadre cadre) {
		return toReferenceDto(cadre);
	}

	@Override
	public void archive(String cadreUuid) {
		Cadre entity = cadreService.getByUuid(cadreUuid);
		entity.setArchived(true);
		cadreService.archiveDearchive(entity);

	}

	@Override
	public void dearchive(String cadreUuid) {
		Cadre entity = cadreService.getByUuid(cadreUuid);
		entity.setArchived(false);
		cadreService.archiveDearchive(entity);
	}

	@Override
	public void delete(Cadre entity) {
		cadreService.delete(entity);
	}

	public static CadreReferenceDto toReferenceDto(Cadre entity) {
		if (entity == null) {
			return null;
		}
		return new CadreReferenceDto(entity.getUuid(), entity.getPosition(), entity.getExternalId());
	}

	public static Cadre fromReferenceDtoToCadre(CadreReferenceDto referenceDto) {
		if (referenceDto == null) {
			return null;
		}
		Cadre cadre = new Cadre();
		cadre.setUuid(referenceDto.getUuid());
		cadre.setPosition(referenceDto.getPosition());
		return cadre;
	}

	@LocalBean
	@Stateless
	public static class CadreFacadeEjbLocal extends CadreFacadeEjb {
	}
}
