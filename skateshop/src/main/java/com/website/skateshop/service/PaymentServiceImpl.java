package com.website.skateshop.service;

import com.website.skateshop.entity.PaymentEntity;
import com.website.skateshop.model.PaymentModel;
import com.website.skateshop.repository.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<PaymentModel> findAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentModel findPaymentById(int id) {
        return paymentRepository.findById(id)
                .map(this::convertToModel)
                .orElse(null);
    }

    @Override
    public List<PaymentModel> findPaymentsByMethod(String method) {
        return paymentRepository.findByMethodIgnoreCase(method).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentModel addPayment(PaymentModel payment) {
        if (payment.getPrice() <= 0) {
            throw new IllegalArgumentException("Сумма платежа должна быть положительной");
        }

        PaymentEntity entity = convertToEntity(payment);
        PaymentEntity saved = paymentRepository.save(entity);
        return convertToModel(saved);
    }

    @Override
    public PaymentModel updatePayment(PaymentModel payment) {
        if (!paymentRepository.existsById(payment.getId())) {
            throw new IllegalArgumentException("Платеж с ID " + payment.getId() + " не найден");
        }

        PaymentEntity entity = convertToEntity(payment);
        PaymentEntity updated = paymentRepository.save(entity);
        return convertToModel(updated);
    }

    @Override
    public void deletePayment(int id) {
        if (!paymentRepository.existsById(id)) {
            throw new IllegalArgumentException("Платеж с ID " + id + " не найден");
        }
        paymentRepository.deleteById(id);
    }

    @Override
    public List<PaymentModel> findPaymentsPaginated(int page, int size) {
        Page<PaymentEntity> result = paymentRepository.findAll(PageRequest.of(page, size));
        return result.getContent().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public long countPayments() {
        return paymentRepository.count();
    }

    private PaymentModel convertToModel(PaymentEntity entity) {
        return new PaymentModel(
                entity.getId(),
                entity.getPrice(),
                entity.getMethod(),
                entity.getPaymentDate()
        );
    }

    private PaymentEntity convertToEntity(PaymentModel model) {
        return new PaymentEntity(
                model.getId(),
                model.getPrice(),
                model.getMethod(),
                model.getPaymentDate()
        );
    }
}