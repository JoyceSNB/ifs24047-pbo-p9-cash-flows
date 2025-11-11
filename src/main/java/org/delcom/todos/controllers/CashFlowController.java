package org.delcom.todos.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.delcom.todos.configs.ApiResponse;
import org.delcom.todos.entities.CashFlow;
import org.delcom.todos.services.CashFlowService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cash-flows")
public class CashFlowController {

    private final CashFlowService cashFlowService;

    public CashFlowController(CashFlowService cashFlowService) {
        this.cashFlowService = cashFlowService;
    }

    private boolean isInvalid(CashFlow cashFlow) {
        return cashFlow.getType() == null || cashFlow.getType().isEmpty() ||
                cashFlow.getSource() == null || cashFlow.getSource().isEmpty() ||
                cashFlow.getLabel() == null || cashFlow.getLabel().isEmpty() ||
                cashFlow.getAmount() == null || cashFlow.getAmount() <= 0 ||
                cashFlow.getDescription() == null || cashFlow.getDescription().isEmpty();
    }

    @PostMapping
    public ApiResponse<Map<String, UUID>> createCashFlow(@RequestBody CashFlow cashFlow) {
        if (isInvalid(cashFlow)) {
            return new ApiResponse<Map<String, UUID>>("fail", "Data tidak valid", null);
        }

        CashFlow newCashFlow = cashFlowService.createCashFlow(cashFlow.getType(), cashFlow.getSource(),
                cashFlow.getLabel(), cashFlow.getAmount(), cashFlow.getDescription());

        return new ApiResponse<Map<String, UUID>>(
                "success",
                "Berhasil menambahkan data",
                Map.of("id", newCashFlow.getId()));
    }

    @GetMapping
    public ApiResponse<Map<String, List<CashFlow>>> getAllCashFlows(
            @RequestParam(required = false) String search) {
        List<CashFlow> cashFlows = cashFlowService.getAllCashFlows(search);

        return new ApiResponse<Map<String, List<CashFlow>>>(
                "success",
                "Berhasil mengambil data",
                Map.of("cashFlows", cashFlows));
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, CashFlow>> getCashFlowById(@PathVariable UUID id) {
        CashFlow cashFlow = cashFlowService.getCashFlowById(id);

        if (cashFlow == null) {
            return new ApiResponse<Map<String, CashFlow>>(
                    "fail",
                    "Data cash flow tidak ditemukan",
                    null);
        }

        return new ApiResponse<Map<String, CashFlow>>(
                "success",
                "Berhasil mengambil data",
                Map.of("cashFlow", cashFlow));
    }

    @GetMapping("/labels")
    public ApiResponse<Map<String, List<String>>> getCashFlowLabels() {
        List<String> labels = cashFlowService.getCashFlowLabels();

        return new ApiResponse<Map<String, List<String>>>(
                "success",
                "Berhasil mengambil data",
                Map.of("labels", labels));
    }

    @PutMapping("/{id}")
    public ApiResponse<CashFlow> updateCashFlow(@PathVariable UUID id, @RequestBody CashFlow cashFlow) {
        if (isInvalid(cashFlow)) {
            return new ApiResponse<CashFlow>("fail", "Data tidak valid", null);
        }

        CashFlow updatedCashFlow = cashFlowService.updateCashFlow(id, cashFlow.getType(),
                cashFlow.getSource(), cashFlow.getLabel(), cashFlow.getAmount(), cashFlow.getDescription());

        if (updatedCashFlow == null) {
            return new ApiResponse<CashFlow>("fail", "Data cash flow tidak ditemukan", null);
        }
        return new ApiResponse<CashFlow>("success", "Berhasil memperbarui data", updatedCashFlow);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCashFlow(@PathVariable UUID id) {
        boolean status = cashFlowService.deleteCashFlow(id);

        if (!status) {
            return new ApiResponse<String>(
                    "fail",
                    "Data cash flow tidak ditemukan",
                    null);
        }

        return new ApiResponse<String>(
                "success",
                "Berhasil menghapus data",
                null);
    }
}

