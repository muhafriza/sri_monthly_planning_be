package sri.sysint.sri_starter_back.controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static sri.sysint.sri_starter_back.security.SecurityConstants.SECRET;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import sri.sysint.sri_starter_back.exception.ResourceNotFoundException;
import sri.sysint.sri_starter_back.model.DetailMarketingOrder;
import sri.sysint.sri_starter_back.model.HeaderMarketingOrder;
import sri.sysint.sri_starter_back.model.MarketingOrder;
import sri.sysint.sri_starter_back.model.Response;
import sri.sysint.sri_starter_back.model.WorkDay;
import sri.sysint.sri_starter_back.model.transaksi.EditMarketingOrderMarketing;
import sri.sysint.sri_starter_back.model.transaksi.SaveMarketingOrderPPC;
import sri.sysint.sri_starter_back.model.view.ViewDetailMarketingOrder;
import sri.sysint.sri_starter_back.model.view.ViewHeaderMarketingOrder;
import sri.sysint.sri_starter_back.model.view.ViewMarketingOrder;
import sri.sysint.sri_starter_back.service.MarketingOrderServiceImpl;

@CrossOrigin(maxAge = 3600)
@RestController
public class MarketingOrderController {
	
	private Response response;	
	
	@Autowired
	private MarketingOrderServiceImpl marketingOrderServiceImpl;
	
	@PersistenceContext	
	private EntityManager em;
		
    private String validateToken(HttpServletRequest req) throws ResourceNotFoundException {
        String header = req.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new ResourceNotFoundException("JWT token not found or maybe not valid");
        }

        String token = header.replace("Bearer ", "");
        String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        return user;
    }
    
    
    //------------------------------------------MARKETING ORDER-------------------------------------

    //GET CAPACITY 
    @GetMapping("/getCapacity")
    public Response getCapacity(final HttpServletRequest req) throws ResourceNotFoundException {
    	validateToken(req);
    	String capacity = marketingOrderServiceImpl.getCapacityValue();
    	response = new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), capacity);
    	return response;
    }
    
    @GetMapping("/getLastIdMo")
    public Response getLastIdMo(final HttpServletRequest req) throws ResourceNotFoundException {
    	validateToken(req);
    	String lastId = marketingOrderServiceImpl.getLastIdMo();
    	response = new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), lastId);
    	return response;
    }
    
    @PostMapping("/saveMarketingOrderPPC")
    public Response saveMarketingOrderPPC(final HttpServletRequest req, @RequestBody SaveMarketingOrderPPC saveMarketingOrderPPC)throws ResourceNotFoundException {
    	validateToken(req);
    	int statusSaved = marketingOrderServiceImpl.saveMarketingOrderPPC(saveMarketingOrderPPC);
    	if(statusSaved == 1) {
    		response = new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), null);
    	}else {
    		response = new Response(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), req.getRequestURI(), null);
    	}
    	return response;
    }
    
    @GetMapping("/getAllMarketingOrderLatest")
	public Response getAllMarketingOrderLatest(final HttpServletRequest req) throws ResourceNotFoundException {
    	validateToken(req);
    	List<MarketingOrder> data = marketingOrderServiceImpl.getAllMarketingOrderLatest();
    	response = new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), data);
	    return response;
	}
    
    //GET ALL MARKETING ORDER LATEST BY ROLE
    @GetMapping("/getAllMarketingOrderMarketing")
    public Response getAllMarketingOrdersMarketing(@RequestParam("role") String role,final HttpServletRequest req) {
        
        List<MarketingOrder> marketingOrders = marketingOrderServiceImpl.getAllMarketingOrderMarketing(role);
        
        Response response = new Response(new Date(), HttpStatus.OK.value(),null, HttpStatus.OK.getReasonPhrase(),req.getRequestURI(), marketingOrders);

        return response;
    }

    @PostMapping("/getMonthAvailable")
    public Response getMonthAvailable(final HttpServletRequest req, @RequestBody Map<String, Object> object) throws ResourceNotFoundException{
    	validateToken(req);
    	
    	String month1 = object.get("month1").toString();
    	String month2 = object.get("month2").toString();
    	String month3 = object.get("month3").toString();
    	String year1 = object.get("year1").toString();
    	String year2 = object.get("year2").toString();
    	String year3 = object.get("year3").toString();
    	String type = object.get("type").toString();

        int availability = marketingOrderServiceImpl.checkMonthsAvailability(month1, month2, month3, year1, year2, year3, type);

        Response response = new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), availability);

        return response;
    }
    
    @GetMapping("/getAllDetailRevisionMo")
    public Response getAllDetailRevisionMo(final HttpServletRequest req, @RequestParam("month0") String month0Str, @RequestParam("month1") String month1Str, @RequestParam("month2") String month2Str, @RequestParam("type") String type) throws ResourceNotFoundException {
    	
    	validateToken(req);

        List<MarketingOrder> data = marketingOrderServiceImpl.getAllMarketingOrder(month0Str, month1Str, month2Str, type);
        
        Response response = new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), data);
        return response;
    }

	
	@PostMapping("/disableMarketingOrder")
	public Response disableMarketingOrder(final HttpServletRequest req, @RequestBody MarketingOrder marketingOrder) throws ResourceNotFoundException {
    	validateToken(req);

	    marketingOrderServiceImpl.disableMarketingOrder(marketingOrder);

        Response response = new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), null);
	    return response;
	}
	
	@PostMapping("/enableMarketingOrder")
	public Response enableMarketingOrder(final HttpServletRequest req, @RequestBody MarketingOrder marketingOrder) throws ResourceNotFoundException {
    	validateToken(req);

	    marketingOrderServiceImpl.enableMarketingOrder(marketingOrder);

        Response response = new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), null);
	    return response;
	}
	
	//--------------------------------HEADER MARKETING ORDER-------------------------------------
     
    @GetMapping("/getHeaderMOById/{id}")
    public Response getHeaderMOById(final HttpServletRequest req, @PathVariable BigDecimal id) throws ResourceNotFoundException {
        validateToken(req);

        Optional<HeaderMarketingOrder> headerMO = marketingOrderServiceImpl.getHeaderMOById(id);
        if (headerMO.isPresent()) {
            response = new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), headerMO.get());
        } else {
            throw new ResourceNotFoundException("HeaderMO not found with id " + id);
        }
        return response;
    }

    //--------------------------------DETAIL MARKETING ORDER-------------------------------------
    
    	@PostMapping("/getDetailMarketingOrders")
	    public Response getDetailMarketingOrders(@RequestBody Map<String, Object> params, final HttpServletRequest req) throws ResourceNotFoundException {
	        validateToken(req);
	
	        // Extract the necessary values from the map
	        String monthYear0 = (String) params.get("monthYear0");
	        String monthYear1 = (String) params.get("monthYear1");
	        String monthYear2 = (String) params.get("monthYear2");

	        BigDecimal totalWdTtM0 = new BigDecimal((String) params.get("totalHKTT1"));
	        BigDecimal totalWdTtM1 = new BigDecimal((String) params.get("totalHKTT2"));
	        BigDecimal totalWdTtM2 = new BigDecimal((String) params.get("totalHKTT3"));
	        BigDecimal totalWdTlM0 = new BigDecimal((String) params.get("totalHKTL1"));
	        BigDecimal totalWdTlM1 = new BigDecimal((String) params.get("totalHKTL2"));
	        BigDecimal totalWdTlM2 = new BigDecimal((String) params.get("totalHKTL3"));
	        String typeMo = (String) params.get("productMerk");
	
	        List<ViewDetailMarketingOrder> detailMarketingOrders = marketingOrderServiceImpl.getDetailMarketingOrders(
	                totalWdTtM0, totalWdTtM1, totalWdTtM2,
	                totalWdTlM0, totalWdTlM1, totalWdTlM2,
	                typeMo, monthYear0, monthYear1, monthYear2
	        );
	
	        Response response = new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), detailMarketingOrders);
	        return response;
	    }

	    
	    @PostMapping("/saveMarketingOrderMarketing")
	    public Response saveMarketingOrderMarketing(final HttpServletRequest req, @RequestBody List<ViewDetailMarketingOrder> detailMOList) throws ResourceNotFoundException {
	        validateToken(req);

	        for (ViewDetailMarketingOrder detail : detailMOList) {
	            System.out.println("Ini lock status dari db" + detail.toString());
	        }
	        
	        
	        // Create a list to hold the response data
	        List<DetailMarketingOrder> MOListProducts = new ArrayList<>();

	        // Map DetailMarketingOrder to ViewMO_ListProduct
	        for (ViewDetailMarketingOrder viewMO : detailMOList) {
	            DetailMarketingOrder detail = new DetailMarketingOrder();
	            
	            // Assign fields from the view model to the entity
	            detail.setDetailId(viewMO.getDetailId());
	            detail.setMoId(viewMO.getMoId());
	            detail.setCategory(viewMO.getCategory());
	            detail.setPartNumber(viewMO.getPartNumber());
	            detail.setDescription(viewMO.getDescription());
	            detail.setMachineType(viewMO.getMachineType());
	            detail.setCapacity(viewMO.getCapacity());
	            detail.setQtyPerMould(viewMO.getQtyPerMould());
	            detail.setQtyPerRak(viewMO.getQtyPerRak());
	            detail.setMinOrder(viewMO.getMinOrder());
	            detail.setMaxCapMonth0(viewMO.getMaxCapMonth0());
	            detail.setMaxCapMonth1(viewMO.getMaxCapMonth1());
	            detail.setMaxCapMonth2(viewMO.getMaxCapMonth2());
	            detail.setInitialStock(viewMO.getInitialStock());
	            detail.setSfMonth0(viewMO.getSfMonth0());
	            detail.setSfMonth1(viewMO.getSfMonth1());
	            detail.setSfMonth2(viewMO.getSfMonth2());
	            detail.setMoMonth0(viewMO.getMoMonth0());
	            detail.setMoMonth1(viewMO.getMoMonth1());
	            detail.setMoMonth2(viewMO.getMoMonth2());
	            detail.setPpd(viewMO.getPpd());
	            detail.setCav(viewMO.getCav());
	            detail.setLockStatusM0(viewMO.getLockStatusM0());
	            detail.setLockStatusM1(viewMO.getLockStatusM1());
	            detail.setLockStatusM2(viewMO.getLockStatusM2());
	            MOListProducts.add(detail);
	            
	        }
	        marketingOrderServiceImpl.updateDetailMOById(MOListProducts);

	        Response response = new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), null);
	        return response;
	    }
	    
	    @PostMapping("/revisiMarketingOrderMarketing")
	    public Response revisiDetailMOMarketing(final HttpServletRequest req, @RequestBody EditMarketingOrderMarketing marketingOrderData) throws ResourceNotFoundException{
	    	validateToken(req);

	    	int statusSaved = marketingOrderServiceImpl.editMarketingOrderMarketing(marketingOrderData);
	    	
	    	if(statusSaved == 1) {
	    		response = new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), null);
	    	}else {
	    		response = new Response(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), req.getRequestURI(), null);
	    	}
	    	return response;
	    }
	    
		@GetMapping("/getAllMoById/{moId}")
		public Response getAllMoById(final HttpServletRequest req, @PathVariable String moId) throws ResourceNotFoundException {
		validateToken(req);
		
		ViewMarketingOrder marketingOrderDetail = marketingOrderServiceImpl.getAllMoById(moId);
			     
			if (marketingOrderDetail != null) {
				response = new Response(new Date(), HttpStatus.OK.value(), null, HttpStatus.OK.getReasonPhrase(), req.getRequestURI(), marketingOrderDetail);
			} else {
				throw new ResourceNotFoundException("DetailMarketingOrder not found with moId " + moId);
			}
			     
			return response;
		 }

		@PostMapping("/getWorkday")
	    public Response getMonthlyWorkData(@RequestBody Map<String, Object> requestBody, final HttpServletRequest req) throws ResourceNotFoundException {
	        validateToken(req);

	        // Mengambil bulan dan tahun dari requestBody
	        int month1 = (int) requestBody.get("month1");
	        int year1 = (int) requestBody.get("year1");
	        int month2 = (int) requestBody.get("month2");
	        int year2 = (int) requestBody.get("year2");
	        int month3 = (int) requestBody.get("month3");
	        int year3 = (int) requestBody.get("year3");

	        // Memanggil service dengan parameter yang diambil dari requestBody
	        List<Map<String, Object>> workDays = marketingOrderServiceImpl.getWorkDay(month1, year1, month2, year2, month3, year3);

	        Response response = new Response(new Date(),HttpStatus.OK.value(),null, HttpStatus.OK.getReasonPhrase(),req.getRequestURI(),workDays);

	        return response;
	    }
	    
	    
	    //EXPORT MARKETING ORDER
	    @RequestMapping("/exportMOExcel/{id}")
		public ResponseEntity<InputStreamResource> exportPLantsExcel(@PathVariable String id) throws IOException {
		 	Optional<MarketingOrder> optionalMarketingOrder = marketingOrderServiceImpl.getMarketingOrderById(id);
	    	MarketingOrder marketingOrder = optionalMarketingOrder.get();
	    	
	        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
	        String MOmonth = monthFormat.format(marketingOrder.getDateValid());
		    String filename = "MO_FED " + "- " + MOmonth + " R" + marketingOrder.getRevisionPpc().toString() + ".xlsx";
		    
		    ByteArrayInputStream data = marketingOrderServiceImpl.exportMOExcel(id);
		    InputStreamResource file = new InputStreamResource(data);
		    
		    return ResponseEntity.ok()
		        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
		        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
		        .body(file);
		}

}
