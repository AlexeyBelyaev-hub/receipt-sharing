package com.alexeybelyaev.receiptsharing.web.controller;

import com.alexeybelyaev.receiptsharing.exceptions.GeneralResponse;
import com.alexeybelyaev.receiptsharing.model.Receipt;
import com.alexeybelyaev.receiptsharing.service.ReceiptService;
import com.alexeybelyaev.receiptsharing.web.dto.ReceiptDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.LazyContextVariable;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Controller
public class ReceiptSharingController {

    private final ReceiptService receiptService;

    @Autowired
    public ReceiptSharingController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    //  -- RECEIPT CONTROLLER --
    @GetMapping("receipt")
    public String getReceiptView(HttpServletRequest request, Model model,
                                 @RequestParam(value = "trybeta", required = false) Boolean trybeta ) {

        if (trybeta!=null && trybeta==true){
            model.addAttribute("receipt", new LazyContextVariable<Receipt>() {
                @Override
                protected Receipt loadValue() {
                    return receiptService.getTestReceipt();
                }
            });
        }

        return "receipt.html";
    }

    //This @RequestBody require jackson-core and jackson-mapper-asl dependencies
    //to parse ReceiptDto
    @PostMapping("user/receipt/save")
    public GeneralResponse saveReceipt(@RequestBody ReceiptDto receiptDto, Principal principal){

        log.debug("Principal = ", principal.getName());

        receiptService.saveReceipt(receiptDto, principal.getName());
        log.debug("REQ", receiptDto);

        return new GeneralResponse("success",
                HttpStatus.OK,
                ZonedDateTime.now(ZoneId.of("Z")));
    }

    @GetMapping("user/receipts")
    public String getUserReceipts(HttpServletRequest request, Model model,
                                  Principal principal) {
        List<ReceiptDto> receiptDtos = receiptService.getUserReceipts(principal.getName());
        model.addAttribute("receipts",receiptDtos);
        return "userReceipts";
    }
}
