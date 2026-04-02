/*
 * @ (#) DoctorDao.java          1.0        4/2/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */

package iuh.fit.dao;

import iuh.fit.entity.Doctor;
import iuh.fit.util.AppUntils;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionContext;
import org.neo4j.driver.summary.ResultSummary;
import org.neo4j.driver.types.Node;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



/*
 * @description:
 * @author: Truong Tran Hung
 * @date: 4/2/2026
 * @version:    1.0
 */
public class DoctorDao {
   public boolean addDoctor(Doctor doctor) {
       String query =
               """
                       CREATE(d:Doctor)
                       SET d.id = $id ,d.name = $name, d.phone = $phone, d.speciality =$speciality
                       RETURN d
               """;
       Map<String, Object> params = Map.of(
               "id", doctor.getId(),
               "name",doctor.getName(),
               "phone",doctor.getPhone(),
               "speciality",doctor.getSpeciality()

       );
       try(Session session = AppUntils.getSession()){
           return session.executeWrite( tx ->{
               ResultSummary resultSummary =tx.run(query, params).consume();
               return resultSummary.counters().nodesCreated() >0;
           });

       }
   }
    public Map<String,Long> getNoOfDoctorsBySpeciality(String departmentName) {
        String query =
                """
                        MATCH (d:Doctor)-[r:BELONG_TO]->(de:Department)
                        WHERE de.name = $depName
                        RETURN d.speciality as speciality, COUNT(d) as total
                        
                """;
        Map<String, Object> params = Map.of(
                "depName", departmentName
        );
        try(Session session = AppUntils.getSession()){
            return session.executeRead(tx ->{
                Result res = tx.run(query, params);
                return res
                        .stream()
                        .collect(Collectors.toMap(
                                r -> r.get("speciality").asString(),
                                r -> r.get("total").asLong()

                        ));
            });

        }
    }

    public List<Doctor>  lisDocTorsBySpeciality (String keyword) {
        String query =
                """
                  CALL db.index.fulltext.queryNodes("specFTI", $keyword) YIELD node
                  RETURN node
                """;
        Map<String, Object> params = Map.of(
                "keyword", keyword

        );
        try(Session session = AppUntils.getSession()){
            return session.executeRead( tx ->{
                Result res = tx.run(query, params);
                return res
                        .stream()
                        .map( r ->  {
                                Node node = r.get("node").asNode();
                                return new Doctor(
                                        node.get("id").asString(),
                                        node.get("name").asString(),
                                        node.get("phone").asString(),
                                        node.get("speciality").asString()

                                );
                        })
                        .toList();
            });


        }
    }

    public static boolean updateDiagnosis(String partientID, String doctorID, String diagnosis) {
        String query =
                """
                        MATCH (d:Doctor)<-[r:BE_TREATED]-(p:Patient)
                        WHERE r.endDate IS NULL AND d.id = $doctorID AND  p.id = $partientID
                        SET r.diagnosis = $diagnosis
                """;
        Map<String, Object> params = Map.of(
                "doctorID", doctorID,
                "partientID", partientID,
                "diagnosis", diagnosis


        );
        try(Session session = AppUntils.getSession()){
                return session.executeWrite(tx->{
                    ResultSummary resultSummary = tx.run(query,params).consume();
                    return resultSummary.counters().propertiesSet() > 0;
                });
        }
    }
public static void main(String[] args) {
    DoctorDao dao = new DoctorDao();

//        Doctor doctor = new Doctor("Dr.788", "Hung", "123457", "Rang Ham Mat");
//        System.out.println(dao.addDoctor(doctor));

//        dao.getNoOfDoctorsBySpeciality("Preventive Medicine")
//                .forEach((k,v) -> System.out.println(k + ": " + v));\

//        dao.lisDocTorsBySpeciality("Internal").forEach(System.out::println );
    System.out.println(updateDiagnosis("PT005","DR.011", "Lalalala"));
}

}
