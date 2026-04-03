/*
* @ (#) DoctorDao.java          1.0        4/3/2026
*
* Copyright (c) 2026 IUH. All rights reserved.
*/

package iuh.fit.dao;

import iuh.fit.entity.Doctor;
import iuh.fit.util.AppUntils;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.summary.ResultSummary;
import org.neo4j.driver.types.Node;

import javax.print.Doc;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
* @description:
* @author: Truong Tran Hung
* @date: 4/3/2026
* @version:    1.0
*/
public class DoctorDao {
    public boolean addDoctor (Doctor doctor){
        String query = """
                CREATE (d:Doctor)
                SET d.id = $id, d.name= $name ,d.phone=$phone, d.sepciality= $sepciality
                RETURN d
                """;
        Map<String, Object> pagrams = Map.of(
                "id",doctor.getId(),
                "name", doctor.getName(),
                "phone", doctor.getPhone(),
                "sepciality",doctor.getSpeciality()
        );
        try(Session session = AppUntils.getSession()){
            return session.executeWrite( tx ->{
                ResultSummary resultSummart = tx.run(query, pagrams).consume();
                return  resultSummart.counters().nodesCreated()>0;

            });

        }
    }
    //Cau b
        public Map<String, Long> getNoODoctorsBySpeciality (String departmentName){
        String query = """
                MATCH(d:Doctor)-[r:BELONG_TO]->(de:Department)
                WHERE de.name = $name
                RETURN d.sepciality AS speciality, count(d) as total
                """;
        Map<String, Object> params = Map.of(
                "name", departmentName
        );
        try (Session session = AppUntils.getSession()){
            return session.executeRead(tx ->{
                Result res = tx.run(query, params);
                return res
                        .stream()
                        .collect(Collectors.toMap(
                                r-> r.get("speciality").asString(),
                                r-> r.get("total").asLong()
                        ));


            });
        }
    }

//    Cau d
    public boolean updateDiadnosis (String patientID, String doctorID, String newDiagnosis){
        String query = """
                MATCH(d:Doctor)<-[r:BELONG_TO]-(p:Patient)
                WHERE r.endDate IS NULL AND d.id = $doctorid AND p.id =$patientid
                SET r.diagnosis = $diagnosis    
                """;
        Map<String, Object> params = Map.of(
                "doctorid", doctorID,
                "patientid",patientID,
                "diagnosis", newDiagnosis
        );
        try (Session session = AppUntils.getSession()){
            return session.executeWrite(tx->{
                ResultSummary resultSumary = tx.run(query, params).consume();
                return resultSumary.counters().propertiesSet() > 0;
            });
        }
    }

    public List<Doctor> listDoctorsBySpeciality (String keyword){
        String query = """
                CALL db.index.fulltext.queryNodes("specFTI", $keyword) YIELD node
                RETURN node
                """;
        Map<String, Object> params = Map.of(
                "keyword", keyword
        );
        try (Session session = AppUntils.getSession()){
            return  session.executeRead(tx ->{
                Result res = tx.run(query, params);
                return  res
                        .stream()
                        .map( r->{
                            Node node  = r.get("node").asNode();
                            return  new Doctor(
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

    public static void main(String[] args) {
        DoctorDao dao = new DoctorDao();

//        Cau a
//        Doctor doctor = new Doctor( "dadsa", "afasf","afsafaf","afsafasf");
//        System.out.println(dao.addDoctor(doctor));
//        System.out.println(dao.updateDiadnosis("HuHu", "Hehe", "ahhahaha"));
        dao.listDoctorsBySpeciality("Community Medicine").forEach(System.out::println);
//        dao.getNoODoctorsBySpeciality("Preventive Medicine")
//                .forEach((k,v)-> System.out.println(k + " " + v));
    }

}
