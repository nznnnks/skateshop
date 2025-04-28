package com.website.skateshop.service;

import com.website.skateshop.entity.PaymentEntity;
import com.website.skateshop.entity.UserEntity;
import com.website.skateshop.model.PaymentModel;
import com.website.skateshop.repository.PaymentRepository;
import com.website.skateshop.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
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
    public PaymentModel addPayment(PaymentModel paymentModel) {
        PaymentEntity entity = convertToEntity(paymentModel);
        PaymentEntity saved = paymentRepository.save(entity);
        return convertToModel(saved);
    }

    @Override
    public PaymentModel updatePayment(PaymentModel paymentModel) {
        PaymentEntity entity = paymentRepository.findById(paymentModel.getId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        entity.setPrice(paymentModel.getPrice());
        entity.setMethod(paymentModel.getMethod());
        entity.setPaymentDate(paymentModel.getPaymentDate());

        PaymentEntity updated = paymentRepository.save(entity);
        return convertToModel(updated);
    }

    @Override
    public void deletePayment(int id) {
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
        PaymentModel model = new PaymentModel();
        model.setId(entity.getId());
        model.setPrice(entity.getPrice());
        model.setMethod(entity.getMethod());
        model.setPaymentDate(entity.getPaymentDate());

        if (entity.getUser() != null) {
            model.setUserId(entity.getUser().getId());
            model.setUserName(entity.getUser().getName() + " " + entity.getUser().getSurname());
        }

        return model;
    }

    private PaymentEntity convertToEntity(PaymentModel model) {
        PaymentEntity entity = new PaymentEntity();
        entity.setId(model.getId());
        entity.setPrice(model.getPrice());
        entity.setMethod(model.getMethod());
        entity.setPaymentDate(model.getPaymentDate());

        if (model.getUserId() != null) {
            UserEntity user = userRepository.findById(model.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            entity.setUser(user);
        }

        return entity;
    }
}