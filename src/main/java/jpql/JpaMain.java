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

            /*
            for (int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member"+ i);
                member.setAge(i);
                em.persist(member);
            }

             */

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setTeam(team);

            em.persist(member);


            /*
            // 두 번째 파라미터로 타입 지정 가능, 기본적으로 엔티티
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);

            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);

            // 타입 정보를 받을 수 없을 때: Query
            Query query3 = em.createQuery("select m.username, m.age from Member m");
             */

            /*
            TypedQuery<Member> query2 = em.createQuery("select m from Member m where id=1L", Member.class);

            List<Member> resultList = query1.getResultList();
            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }

            Member singleResult = query2.getSingleResult();
            System.out.println("singleResult = " + singleResult);
            */

            /*
            // 파라미터 바인딩 - 메소드 체인 활용
            Member singleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("singleResult = " + singleResult.getUsername());
             */

            em.flush();
            em.clear();


            /*
            // 엔티티 프로젝션1
            List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();
            // em.flush(), em.clear() 후에 setAge() 로 값이 바뀌면 List<Member> 는 영속성 컨텍스트에서 관리가 되는 것이고 바뀌지 않으면 관리가 안되는 것
            // 실행결과: update 쿼리 발생, 결론: 영속성 컨텍스트에서 관리됨
            Member findMember = result.get(0);
            findMember.setAge(20);
             */

            /*
            // 엔티티 프로젝션2
            // JPQL 은 아래와 같지만 실제 SQL 은 Member 와 Team 을 Join 한다
            List<Team> result = em.createQuery("select m.team from Member m", Team.class).getResultList();
            // 같은 결과가 나오지만 join 은 성능에 영향을 많이 주기 때문에 한눈에 보이는 것이 좋다. 웬만하면 아래와 같이 명시적으로 작성한다
            List<Team> result2 = em.createQuery("select t from Member m join m.team t", Team.class).getResultList();
             */

            /*
            // 임베디드 타입 프로젝션
            // 프로젝션 Order 안의 address 관련된 것만 쿼리해온다
            em.createQuery("select o.address from Order o", Address.class).getResultList();
             */

            /*
            // 스칼라 타입 프로젝션
//            em.createQuery("select distinct m.username, m.age from Member m").getResultList();
//            List resultList = em.createQuery("select m.username, m.age from Member m").getResultList();

//            Object o = resultList.get(0);
//            Object[] result = (Object[]) o;
            // 타입을 지정하지 못하므로 캐스팅 Object[] 캐스팅 필요 또는 List<Object[]>
//            List<Object[]> resultList = em.createQuery("select m.username, m.age from Member m").getResultList();

//            System.out.println("username = " + result[0]);
//            System.out.println("age = " + result[1]);

            // DTO 만들어서 조회
            List<MemberDTO> resultList = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class).getResultList();
            MemberDTO memberDTO = resultList.get(0);
            System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());

             */

            /*
            // 페이징
            List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(1).setMaxResults(10).getResultList();

            System.out.println("result.size() = " + result.size());
            for (Member member1 : result) {
                System.out.println("member1 = " + member1);
            }
             */


//            String query = "select m from Member m left outer join m.team t";
            // 세타 조인
//            String query = "select m from Member m, Team t where m.username = t.name";

            // 조인 대상 필터링
//            String query = "select m from Member m left join m.team t on t.name = 'teamA'";

            // 연관 관계 없는 엔티티 외부 조인
            String query = "select m from Member m left join Team t on m.username = t.name";




            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

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
