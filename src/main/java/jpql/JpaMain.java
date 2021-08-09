package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        // META-INF/persistence.xml 에 설정해 놓은 persistence-unit name 을 전달한다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            // 영속

            Member member1 = new Member();
            member1.setUsername("관리자1");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            em.persist(member2);

            em.flush();
            em.clear();

/*
//            String query = "select concat('a', 'b') From Member m";
            String query = "select substring(m.username, 2, 3) From Member m";
            List<String> result = em.createQuery(query, String.class).getResultList();
            for (String s : result) {
                System.out.println("s = " + s);
            }

 */

            /*
            String query = "select locate('de', 'abcdef') From Member m";
            List<Integer> result = em.createQuery(query, Integer.class).getResultList();
            for (Integer s : result) {
                System.out.println("s = " + s);
            }
             */

            /*
            String query = "select size(t.members) From Team t";
            List<Integer> result = em.createQuery(query, Integer.class).getResultList();
            for (Integer s : result) {
                System.out.println("s = " + s);
            }
             */


//            String query = "select function('group_concat', m.username) From Member m";
            String query = "select group_concat(m.username) From Member m";

            List<String> result = em.createQuery(query, String.class).getResultList();
            for (String s : result) {
                System.out.println("s = " + s);
            }


            System.out.println("=============================");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
