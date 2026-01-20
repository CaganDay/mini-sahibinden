package com.minisahibinden.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.minisahibinden.entity.Listing;
import com.minisahibinden.entity.RealEstate;
import com.minisahibinden.entity.User;
import com.minisahibinden.entity.Vehicle;
import com.minisahibinden.repository.ListingRepository;
import com.minisahibinden.repository.RealEstateRepository;
import com.minisahibinden.repository.UserRepository;
import com.minisahibinden.repository.VehicleRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    private static final int PAGE_SIZE = 12; // Show 12 items per page for better performance

    private final VehicleRepository vehicleRepository;
    private final RealEstateRepository realEstateRepository;
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    // All 81 cities of Turkey
    private static final List<String> TURKEY_CITIES = Arrays.asList(
        "Adana", "Adiyaman", "Afyonkarahisar", "Agri", "Aksaray", "Amasya", "Ankara", "Antalya", "Ardahan", "Artvin",
        "Aydin", "Balikesir", "Bartin", "Batman", "Bayburt", "Bilecik", "Bingol", "Bitlis", "Bolu", "Burdur",
        "Bursa", "Canakkale", "Cankiri", "Corum", "Denizli", "Diyarbakir", "Duzce", "Edirne", "Elazig", "Erzincan",
        "Erzurum", "Eskisehir", "Gaziantep", "Giresun", "Gumushane", "Hakkari", "Hatay", "Igdir", "Isparta", "Istanbul",
        "Izmir", "Kahramanmaras", "Karabuk", "Karaman", "Kars", "Kastamonu", "Kayseri", "Kilis", "Kirikkale", "Kirklareli",
        "Kirsehir", "Kocaeli", "Konya", "Kutahya", "Malatya", "Manisa", "Mardin", "Mersin", "Mugla", "Mus",
        "Nevsehir", "Nigde", "Ordu", "Osmaniye", "Rize", "Sakarya", "Samsun", "Sanliurfa", "Siirt", "Sinop",
        "Sirnak", "Sivas", "Tekirdag", "Tokat", "Trabzon", "Tunceli", "Usak", "Van", "Yalova", "Yozgat", "Zonguldak"
    );

    public HomeController(VehicleRepository vehicleRepository, RealEstateRepository realEstateRepository,
                         ListingRepository listingRepository, UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.realEstateRepository = realEstateRepository;
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(Model model,
                       @RequestParam(name = "keyword", required = false) String keyword,
                       @RequestParam(name = "year", required = false) Integer year,
                       @RequestParam(name = "city", required = false) String city,
                       @RequestParam(name = "roomConfig", required = false) String roomConfig,
                       @RequestParam(name = "vehiclePage", defaultValue = "0") int vehiclePage,
                       @RequestParam(name = "realestatePage", defaultValue = "0") int realestatePage,
                       @RequestParam(name = "tab", required = false) String tab,
                       // Vehicle filter parameters
                       @RequestParam(name = "minYear", required = false) Integer minYear,
                       @RequestParam(name = "maxYear", required = false) Integer maxYear,
                       @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
                       @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
                       @RequestParam(name = "minKm", required = false) Integer minKm,
                       @RequestParam(name = "maxKm", required = false) Integer maxKm,
                       @RequestParam(name = "modelName", required = false) String modelName,
                       // Real Estate filter parameters
                       @RequestParam(name = "filterCity", required = false) String filterCity,
                       @RequestParam(name = "filterRoomConfig", required = false) String filterRoomConfig,
                       @RequestParam(name = "filterSellerType", required = false) String filterSellerType,
                       @RequestParam(name = "minPriceRe", required = false) BigDecimal minPriceRe,
                       @RequestParam(name = "maxPriceRe", required = false) BigDecimal maxPriceRe,
                       @RequestParam(name = "minArea", required = false) Integer minArea,
                       @RequestParam(name = "maxArea", required = false) Integer maxArea) {

        Pageable vehiclePageable = PageRequest.of(vehiclePage, PAGE_SIZE);
        Pageable realestatePageable = PageRequest.of(realestatePage, PAGE_SIZE);

        // Vehicle filters
        List<Integer> years = vehicleRepository.getDistinctYears();
        Page<Vehicle> vehiclesPage;

        // Check if any advanced filter is applied
        boolean hasAdvancedFilter = minYear != null || maxYear != null || 
                                    minPrice != null || maxPrice != null || 
                                    minKm != null || maxKm != null ||
                                    (modelName != null && !modelName.isEmpty());

        if (hasAdvancedFilter) {
            // Use advanced filter with all parameters
            vehiclesPage = vehicleRepository.filterVehiclesPaged(
                    minYear, maxYear, minPrice, maxPrice, minKm, maxKm, modelName, vehiclePageable);
        } else if (keyword != null && !keyword.isEmpty() && !"realestate".equals(tab)) {
            vehiclesPage = vehicleRepository.searchByModelNamePaged(keyword, vehiclePageable);
        } else if (year != null) {
            vehiclesPage = vehicleRepository.filterByYearPaged(year, vehiclePageable);
        } else {
            vehiclesPage = vehicleRepository.findAllActivePaged(vehiclePageable);
        }

        // Get filter ranges for the UI
        Object[] filterRanges = vehicleRepository.getFilterRanges();
        if (filterRanges != null && filterRanges.length > 0) {
            Object[] ranges = (Object[]) filterRanges[0];
            model.addAttribute("yearMin", ranges[0]);
            model.addAttribute("yearMax", ranges[1]);
            model.addAttribute("priceMin", ranges[2]);
            model.addAttribute("priceMax", ranges[3]);
            model.addAttribute("kmMin", ranges[4]);
            model.addAttribute("kmMax", ranges[5]);
        }

        // Get distinct model names for filter
        List<String> modelNames = vehicleRepository.getDistinctModelNames();
        model.addAttribute("modelNames", modelNames);

        // RealEstate filters
        List<String> cities = realEstateRepository.getDistinctCitiesFromDb();
        List<String> roomConfigs = realEstateRepository.getDistinctRoomConfigs();
        List<String> sellerTypes = realEstateRepository.getDistinctSellerTypes();
        Page<RealEstate> realEstatesPage;

        // Check if any real estate advanced filter is applied
        boolean hasReAdvancedFilter = (filterCity != null && !filterCity.isEmpty()) ||
                                      (filterRoomConfig != null && !filterRoomConfig.isEmpty()) ||
                                      (filterSellerType != null && !filterSellerType.isEmpty()) ||
                                      minPriceRe != null || maxPriceRe != null ||
                                      minArea != null || maxArea != null;

        if (hasReAdvancedFilter) {
            // Use advanced filter with all parameters
            realEstatesPage = realEstateRepository.filterRealEstatePaged(
                    filterCity, filterRoomConfig, filterSellerType,
                    minPriceRe, maxPriceRe, minArea, maxArea, realestatePageable);
        } else if (keyword != null && !keyword.isEmpty() && "realestate".equals(tab)) {
            realEstatesPage = realEstateRepository.searchByLocationPaged(keyword, realestatePageable);
        } else if (city != null && !city.isEmpty()) {
            realEstatesPage = realEstateRepository.findByCityPaged(city, realestatePageable);
        } else if (roomConfig != null && !roomConfig.isEmpty()) {
            realEstatesPage = realEstateRepository.findByRoomConfigPaged(roomConfig, realestatePageable);
        } else {
            realEstatesPage = realEstateRepository.findAllActivePaged(realestatePageable);
        }

        // Get real estate filter ranges for the UI
        Object[] reFilterRanges = realEstateRepository.getRealEstateFilterRanges();
        if (reFilterRanges != null && reFilterRanges.length > 0) {
            Object[] reRanges = (Object[]) reFilterRanges[0];
            model.addAttribute("rePriceMin", reRanges[0]);
            model.addAttribute("rePriceMax", reRanges[1]);
            model.addAttribute("areaMin", reRanges[2]);
            model.addAttribute("areaMax", reRanges[3]);
        }

        // Determine active tab
        String activeTab = "vehicles";
        if ("realestate".equals(tab) || city != null || roomConfig != null || hasReAdvancedFilter) {
            activeTab = "realestate";
        }

        // Add to model
        model.addAttribute("years", years);
        model.addAttribute("vehicles", vehiclesPage.getContent());
        model.addAttribute("vehiclesPage", vehiclesPage);
        model.addAttribute("cities", cities);
        model.addAttribute("roomConfigs", roomConfigs);
        model.addAttribute("sellerTypes", sellerTypes);
        model.addAttribute("realEstates", realEstatesPage.getContent());
        model.addAttribute("realEstatesPage", realEstatesPage);
        model.addAttribute("activeTab", activeTab);
        model.addAttribute("hasAdvancedFilter", hasAdvancedFilter);
        model.addAttribute("hasReAdvancedFilter", hasReAdvancedFilter);

        return "home";
    }

    @GetMapping("/vehicle/{id}")
    public String vehicleDetail(@PathVariable Integer id, Model model) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id:" + id));
        model.addAttribute("vehicle", vehicle);
        return "vehicle-detail";
    }

    @GetMapping("/realestate/{id}")
    public String realEstateDetail(@PathVariable Integer id, Model model) {
        RealEstate realEstate = realEstateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid real estate Id:" + id));
        model.addAttribute("realEstate", realEstate);
        return "realestate-detail";
    }

    @GetMapping("/post")
    public String showPostAdForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("loggedInUser") == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to post an ad.");
            return "redirect:/login";
        }
        model.addAttribute("cities", TURKEY_CITIES);
        return "post-ad";
    }

    @PostMapping("/post/vehicle")
    public String postVehicle(@RequestParam String modelName,
                              @RequestParam Integer modelYear,
                              @RequestParam BigDecimal price,
                              @RequestParam Integer kilometers,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to post an ad.");
            return "redirect:/login";
        }
        // Create listing
        Listing listing = new Listing(user, price, LocalDate.now(), Listing.Category.Vehicle);
        listing = listingRepository.save(listing);
        // Create vehicle
        Vehicle vehicle = new Vehicle(listing, modelYear, modelName, kilometers);
        vehicleRepository.save(vehicle);
        redirectAttributes.addFlashAttribute("success", "Vehicle ad posted successfully!");
        return "redirect:/post";
    }

    @PostMapping("/post/realestate")
    public String postRealEstate(@RequestParam String city,
                                 @RequestParam String district,
                                 @RequestParam(required = false) String neighborhood,
                                 @RequestParam Integer areaSqm,
                                 @RequestParam String roomConfig,
                                 @RequestParam BigDecimal price,
                                 @RequestParam String sellerType,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to post an ad.");
            return "redirect:/login";
        }
        // Create listing
        Listing listing = new Listing(user, price, LocalDate.now(), Listing.Category.RealEstate);
        listing = listingRepository.save(listing);
        // Create real estate
        RealEstate realEstate = new RealEstate(listing, sellerType, areaSqm, roomConfig,
                city, district, neighborhood != null ? neighborhood : "Unknown");
        realEstateRepository.save(realEstate);
        redirectAttributes.addFlashAttribute("success", "Real estate ad posted successfully!");
        return "redirect:/post";
    }
}
