package org.okten;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Consumer;

public class Main {

    private static EntityManagerFactory emf;

    // CRUD - create (insert), read (select), update, delete

    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("xxx-database");

        execute(em -> {
            Product product = new Product();
            product.setName("test");
            product.setPrice(1.99);
            em.persist(product);
        });

        execute(em -> {
            Product product = em.find(Product.class, 1L);
            product.setName("test2");

            em.detach(product);
            product.setPrice(9.98);
        });

        execute(em -> {
            Product product = em.find(Product.class, 1L);
            em.remove(product);
        });

        execute(em -> {
            Customer customer = new Customer();
            customer.setName("test customer");

            Address address = new Address();
            address.setCity("Kyiv");
            address.setStreet("Volodymyrska 132121");

            customer.setAddress(address);

            Order order1 = new Order();
            order1.setOrderDate(OffsetDateTime.now());
            order1.setCustomer(customer);

            Order order2 = new Order();
            order2.setOrderDate(OffsetDateTime.now().minusDays(2));
            order2.setCustomer(customer);

            customer.setOrders(List.of(order1, order2));

            em.persist(customer);
        });

        execute(em -> {
            Product product = new Product();
            product.setName("test product 2");
            product.setPrice(123.99);

            Tag tag1 = new Tag();
            tag1.setName("tag1");

            Tag tag2 = new Tag();
            tag2.setName("tag2");

            product.setTags(List.of(tag1, tag2));

            em.persist(tag1);
            em.persist(tag2);
            em.persist(product);
        });
    }

    private static void execute(Consumer<EntityManager> action) {
        try (EntityManager em = emf.createEntityManager()) {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();

            action.accept(em);

            transaction.commit();
        }
    }
}