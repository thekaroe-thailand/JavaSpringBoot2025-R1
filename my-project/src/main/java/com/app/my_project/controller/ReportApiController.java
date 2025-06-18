package com.app.my_project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import com.app.my_project.repository.BillSaleRepository;
import com.app.my_project.entity.BillSaleEntity;
import com.app.my_project.entity.BillSaleDetailEntity;
import com.app.my_project.repository.BillSaleDetailRepository;
import org.springframework.web.bind.annotation.PutMapping;
import com.app.my_project.repository.ProductionRepository;
import com.app.my_project.entity.ProductionEntity;

@RestController
@RequestMapping("/api/report")
public class ReportApiController {

    @Autowired
    private BillSaleRepository billSaleRepository;

    @Autowired
    private BillSaleDetailRepository billSaleDetailRepository;

    @Autowired
    private ProductionRepository productionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public record IncomePerMonth(int month, double income) {
    }

    @GetMapping("/sum-income-per-month/{year}")
    @SuppressWarnings("unchecked")
    public List<IncomePerMonth> sumIncomePerMonth(@PathVariable int year) {
        String sql = """
                    SELECT
                        EXTRACT(MONTH FROM bse.created_at) AS month,
                        SUM(bsd.quantity * bsd.price) AS income
                    FROM bill_sale_detail_entity bsd
                    LEFT JOIN bill_sale_entity bse ON bsd.bill_sale_id = bse.id
                    WHERE bse.status = 'paid'
                    AND EXTRACT(YEAR FROM bse.created_at) = :year
                    GROUP BY EXTRACT(MONTH FROM bse.created_at)
                """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("year", year);

        List<Object[]> results = query.getResultList();
        List<IncomePerMonth> response = new ArrayList<>();

        for (Object[] row : results) {
            int month = ((Number) row[0]).intValue();
            double income = ((Number) row[1]).doubleValue();

            response.add(new IncomePerMonth(month, income));
        }

        return response;
    }

    @GetMapping("/bill-sales")
    public List<BillSaleEntity> billSalePerMonth() {
        return billSaleRepository.findAll();
    }

    @GetMapping("/bill-sale-detail/{billSaleId}")
    public List<BillSaleDetailEntity> billSaleDetail(@PathVariable Long billSaleId) {
        return billSaleDetailRepository.findAllByBillSaleId(billSaleId);
    }

    @DeleteMapping("/bill-sale/{billSaleId}")
    public void deleteBillSale(@PathVariable Long billSaleId) {
        BillSaleEntity billSale = billSaleRepository.findById(billSaleId)
                .orElseThrow(() -> new RuntimeException("Bill sale not found"));
        billSale.setStatus("cancel");
        billSaleRepository.save(billSale);
    }

    @PutMapping("/bill-sale/{billSaleId}")
    public void updateBillSale(@PathVariable Long billSaleId) {
        BillSaleEntity billSale = billSaleRepository.findById(billSaleId)
                .orElseThrow(() -> new RuntimeException("Bill sale not found"));
        billSale.setStatus("paid");
        billSaleRepository.save(billSale);
    }

    @GetMapping("/dashboard")
    @SuppressWarnings("unchecked")
    public Object dashboard() {
        try {
            // การผลิตทั้งหมด
            String sql = """
                        SELECT
                            COALESCE(SUM(production_log_entity.qty), 0) AS sum_qty
                        FROM production_log_entity
                    """;

            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> results = query.getResultList();
            Object sumQty = results.get(0);

            // ยอดขายทั้งหมด
            sql = """
                    SELECT
                        COALESCE(SUM(bsd.quantity * bsd.price), 0) AS sum_income
                    FROM bill_sale_detail_entity bsd
                    LEFT JOIN bill_sale_entity bse ON bsd.bill_sale_id = bse.id
                    WHERE bse.status = 'paid'
                    """;

            query = entityManager.createNativeQuery(sql);
            results = query.getResultList();
            Object sumIncome = results.get(0);

            // รายการสินค้าทั้งหมด
            int totalProduct = productionRepository.findAll().size();

            // loss ทั้งหมด
            sql = """
                    SELECT
                        SUM(production_loss_entity.qty) AS sum_qty
                    FROM production_loss_entity
                    """;
            query = entityManager.createNativeQuery(sql);
            results = query.getResultList();
            Object sumLoss = results.get(0);

            record Response(Object sumQty, Object sumIncome, int totalProduct, Object sumLoss) {
            }
            return new Response(sumQty, sumIncome, totalProduct, sumLoss);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
