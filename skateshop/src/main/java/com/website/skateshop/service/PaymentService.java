package com.website.skateshop.service;

import com.website.skateshop.model.PaymentModel;
import java.util.List;

public interface PaymentService {
    List<PaymentModel> findAllPayments();
    PaymentModel findPaymentById(int id);
    List<PaymentModel> findPaymentsByMethod(String method);
    PaymentModel addPayment(PaymentModel payment);
    PaymentModel updatePayment(PaymentModel payment);
    void deletePayment(int id);
    List<PaymentModel> findPaymentsPaginated(int page, int size);
    long countPayments();
}