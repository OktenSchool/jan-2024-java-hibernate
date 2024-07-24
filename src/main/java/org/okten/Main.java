package org.okten;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Main {

    public static void main(String[] args) {
        try (StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure()
                .build()) {
            Metadata metadata = new MetadataSources(serviceRegistry)
                    .addAnnotatedClass(Product.class)
                    .getMetadataBuilder()
                    .build();

            try (SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
                 Session session = sessionFactory.openSession()) {

                Transaction transaction = session.beginTransaction();

                Product product = new Product();
                product.setName("Laptop1");
                product.setPrice(3999.99);
                session.persist(product); // INSERT

                product.setPrice(3499.99);
                session.merge(product); // UPDATE

                session.detach(product);
                product.setPrice(2399D);
                session.remove(product); // DELETE

                session.persist(product); // INSERT

                transaction.commit();
            } catch (HibernateException e) {
                throw new RuntimeException(e);
            }
        }
    }
}