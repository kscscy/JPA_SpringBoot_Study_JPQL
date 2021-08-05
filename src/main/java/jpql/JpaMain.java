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

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            String query = "select (select avg(m1.age) From Member m1) as avgAge from Member m join Team t on m.username = t.name";

            // From 절 서브쿼리 사용 불가능
//            String query2 = "select mm.age, mm.username " +
//                            " from (select m.age, m.username from Member m) as mm";

            List<Member> result = em.createQuery(query, Member.class).getResultList();
            List<Member> result2 = em.createQuery(query2, Member.class).getResultList();

            System.out.println("result = " + result.size());


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
