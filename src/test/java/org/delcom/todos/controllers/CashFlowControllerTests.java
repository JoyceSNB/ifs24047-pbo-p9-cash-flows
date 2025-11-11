package org.delcom.todos.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.delcom.todos.configs.ApiResponse;
import org.delcom.todos.entities.CashFlow;
import org.delcom.todos.services.CashFlowService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CashFlowControllerTests {
    @Test
    @DisplayName("Pengujian untuk controller CashFlow")
    void testCashFlowController() throws Exception {
        UUID cashFlowId = UUID.randomUUID();
        UUID nonexistentCashFlowId = UUID.randomUUID();

        CashFlow cashFlow = new CashFlow("Pemasukan", "Gaji", "Gaji Bulanan", 5000000, "Gaji bulan ini");
        cashFlow.setId(cashFlowId);

        CashFlowService cashFlowService = Mockito.mock(CashFlowService.class);

        when(cashFlowService.createCashFlow(any(String.class), any(String.class), any(String.class), any(Integer.class),
                any(String.class))).thenReturn(cashFlow);

        CashFlowController cashFlowController = new CashFlowController(cashFlowService);
        assert (cashFlowController != null);

        {
            ApiResponse<Map<String, UUID>> result = cashFlowController.createCashFlow(cashFlow);
            assert (result != null);
            assert (result.getStatus().equals("success"));
            assert (result.getData().get("id").equals(cashFlowId));
        }

        List<CashFlow> invalidCashFlows = List.of(

                new CashFlow(null, "Source", "Label", 1000, "Description"),
                new CashFlow("", "Source", "Label", 1000, "Description"),
                new CashFlow("Type", null, "Label", 1000, "Description"),
                new CashFlow("Type", "", "Label", 1000, "Description"),
                new CashFlow("Type", "Source", null, 1000, "Description"),
                new CashFlow("Type", "Source", "", 1000, "Description"),
                new CashFlow("Type", "Source", "Label", null, "Description"),
                new CashFlow("Type", "Source", "Label", 0, "Description"),
                new CashFlow("Type", "Source", "Label", 1000, null),
                new CashFlow("Type", "Source", "Label", 1000, ""));
        {
            ApiResponse<Map<String, UUID>> result;
            for (CashFlow cf : invalidCashFlows) {
                result = cashFlowController.createCashFlow(cf);
                assert (result != null);
                assert (result.getStatus().equals("fail"));
            }
        }

        {
            List<CashFlow> dummyResponse = List.of(cashFlow);
            when(cashFlowService.getAllCashFlows("testing")).thenReturn(dummyResponse);
            ApiResponse<Map<String, List<CashFlow>>> result = cashFlowController.getAllCashFlows(null);
            assert (result != null);
            assert (result.getStatus().equals("success"));
            assert (result.getData().size() == 1);
        }

        {
            when(cashFlowService.getCashFlowById(cashFlowId)).thenReturn(cashFlow);
            ApiResponse<Map<String, CashFlow>> result = cashFlowController.getCashFlowById(cashFlowId);
            assert (result != null);
            assert (result.getStatus().equals("success"));
            assert (result.getData().get("cashFlow").getId().equals(cashFlowId));
        }

        {
            when(cashFlowService.getCashFlowById(nonexistentCashFlowId)).thenReturn(null);
            ApiResponse<Map<String, CashFlow>> result = cashFlowController.getCashFlowById(nonexistentCashFlowId);
            assert (result != null);
            assert (result.getStatus().equals("fail"));
        }

        {
            List<String> dummyLabels = List.of("Gaji", "Investasi", "Hadiah");
            when(cashFlowService.getCashFlowLabels()).thenReturn(dummyLabels);
            ApiResponse<Map<String, List<String>>> result = cashFlowController.getCashFlowLabels();
            assert (result != null);
            assert (result.getStatus().equals("success"));
            assert (result.getData().get("labels").size() == 3);
        }

        {
            CashFlow updatedCashFlow = new CashFlow("Pemasukan", "Gaji", "Deskripsi updated", 5000, "2023-01-01");
            updatedCashFlow.setId(cashFlowId);
            when(cashFlowService.updateCashFlow(any(UUID.class), any(String.class), any(String.class),
                    any(String.class),
                    any(Integer.class), any(String.class)))
                    .thenReturn(updatedCashFlow);

            ApiResponse<CashFlow> result = cashFlowController.updateCashFlow(cashFlowId, updatedCashFlow);
            assert (result != null);
            assert (result.getStatus().equals("success"));
        }

        {
            ApiResponse<CashFlow> result;
            for (CashFlow cf : invalidCashFlows) {
                cf.setId(cashFlowId);
                result = cashFlowController.updateCashFlow(cashFlowId, cf);
                assert (result != null);
                assert (result.getStatus().equals("fail"));
            }
        }

        {
            when(cashFlowService.updateCashFlow(any(UUID.class), any(String.class), any(String.class),
                    any(String.class),
                    any(Integer.class), any(String.class)))
                    .thenReturn(null);
            CashFlow updatedCashFlow = new CashFlow("Belajar Spring Boot - Updated", "Deskripsi updated", "Belajar",
                    1000,
                    "Belajar");
            updatedCashFlow.setId(nonexistentCashFlowId);

            ApiResponse<CashFlow> result = cashFlowController.updateCashFlow(nonexistentCashFlowId, updatedCashFlow);
            assert (result != null);
            assert (result.getStatus().equals("fail"));
        }

        {
            when(cashFlowService.deleteCashFlow(cashFlowId)).thenReturn(true);
            ApiResponse<String> result = cashFlowController.deleteCashFlow(cashFlowId);
            assert (result != null);
            assert (result.getStatus().equals("success"));
        }

        {
            when(cashFlowService.deleteCashFlow(nonexistentCashFlowId)).thenReturn(false);
            ApiResponse<String> result = cashFlowController.deleteCashFlow(nonexistentCashFlowId);
            assert (result != null);
            assert (result.getStatus().equals("fail"));
        }
    }
}