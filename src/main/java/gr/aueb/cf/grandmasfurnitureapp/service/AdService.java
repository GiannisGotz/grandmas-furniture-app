package gr.aueb.cf.grandmasfurnitureapp.service;

import gr.aueb.cf.grandmasfurnitureapp.core.filters.AdFilters;
import gr.aueb.cf.grandmasfurnitureapp.core.filters.Paginated;
import gr.aueb.cf.grandmasfurnitureapp.core.specifications.AdSpecification;
import gr.aueb.cf.grandmasfurnitureapp.dto.AdReadOnlyDTO;
import gr.aueb.cf.grandmasfurnitureapp.mapper.Mapper;
import gr.aueb.cf.grandmasfurnitureapp.model.Ad;
import gr.aueb.cf.grandmasfurnitureapp.repository.AdRepository;
import gr.aueb.cf.grandmasfurnitureapp.repository.AttachmentRepository;
import gr.aueb.cf.grandmasfurnitureapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AdService.class);
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;
    private final Mapper mapper;


    public Page<AdReadOnlyDTO> getPaginatedAds(int page, int size) {
        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());
        return adRepository.findAll(pageable).map(mapper::mapToAdReadOnlyDTO);
    }

    @Transactional
    public Page<AdReadOnlyDTO> getPaginatedSortedAds(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return adRepository.findAll(pageable).map(mapper::mapToAdReadOnlyDTO);
    }

    @Transactional
    public List<AdReadOnlyDTO> getAdsFiltered(AdFilters filters) {
        return adRepository.findAll(getSpecsFromFilters(filters))
                .stream().map(mapper::mapToAdReadOnlyDTO).collect(Collectors.toList());
    }

    @Transactional
    public Paginated<AdReadOnlyDTO> getAdsFilteredPaginated(AdFilters filters) {
        var filtered = adRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        return new Paginated<>(filtered.map(mapper::mapToAdReadOnlyDTO));
    }

    private Specification<Ad> getSpecsFromFilters(AdFilters filters) {
        return AdSpecification.adTitleLike(filters.getTitle())
                .and(AdSpecification.adCategoryIs(filters.getCategoryId()))
                .and(AdSpecification.adCategoryNameLike(filters.getCategoryName()))
                .and(AdSpecification.adConditionIs(filters.getCondition()))
                .and(AdSpecification.adPriceBetween(filters.getMinPrice(), filters.getMaxPrice()))
                .and(AdSpecification.adCityIs(filters.getCityId()))
                .and(AdSpecification.adCityNameLike(filters.getCityName()))
                .and(AdSpecification.adUserIs(filters.getUserId()))
                .and(AdSpecification.adIsAvailable(filters.getIsAvailable()))
                .and(AdSpecification.adDescriptionLike(filters.getDescription()));
    }

}
