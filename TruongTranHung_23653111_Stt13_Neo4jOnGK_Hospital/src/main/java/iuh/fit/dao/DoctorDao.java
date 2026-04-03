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

    //a, CREATE → SET → Map → executeWrite → nodesCreated > 0
    public boolean addDoctor (Doctor doctor){
        String query = """
                CREATE (d:Doctor)
                    SET d.id=$id, d.name = $name, d.phone= $phone, d.sepcialitys=  $sepcialitys
                    RETURN d
                """;
        //Tạo Map để truyền tham số vào câu Cypher
        Map<String, Object > params = Map.of(
                "id",doctor.getId(),
                "name",doctor.getName(),
                "phone",doctor.getPhone(),
                "sepcialitys",doctor.getSpeciality()
        );
        //Mở kết nối đến database (Neo4j)
        try(Session session = AppUntils.getSession()){
            return session.executeWrite( tx->{
                //Chạy câu Cypher:
                ResultSummary resultSummary = tx.run(query, params).consume();
                //Kiem tra Có tạo node thành công không
                return resultSummary.counters().nodesCreated() > 0;
            });

        }
    }
    //b
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
        //Mở session
        try(Session session = AppUntils.getSession()){
            //Đây là truy vấn chỉ đọc dữ liệu
            return session.executeRead(tx ->{
//                Gửi câu Cypher lên Neo4j
//                nhận về kết quả dạng TABLE
                Result res = tx.run(query, params);
                return res
                        //chuyển bảng kết quả thành từng dòngc
                        .stream()
                        //gom tất cả record lại thành Map
                        .collect(Collectors.toMap(
                                r -> r.get("speciality").asString(),
                                r -> r.get("total").asLong()

                        ));
            });

        }
    }
    //c
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
                        //Biến bảng kết quả thành từng dòng
                        .stream()
                        //chuyển mỗi record → 1 object Doctor
                        .map( r ->  {
                            //Chuyển từ Neo4j Node → Java Object
                            Node node = r.get("node").asNode();
                            //chuyển mỗi record → 1 object Doctor
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
    //d,
    public static boolean updateDiagnosis(String partientID, String doctorID, String newDiagnosis) {
        String query =
                """
                        MATCH (d:Doctor)<-[r:BE_TREATED]-(p:Patient)
                        WHERE r.endDate IS NULL AND d.id = $doctorID AND  p.id = $partientID
                        SET r.diagnosis = $diagnosis
                """;
        Map<String, Object> params = Map.of(
                "doctorID", doctorID,
                "partientID", partientID,
                "diagnosis", newDiagnosis


        );
        try(Session session = AppUntils.getSession()){
            return session.executeWrite(tx->{
                ResultSummary resultSummary = tx.run(query,params).consume();//“consume = chỉ quan tâm query chạy thành công, không cần data”
                return resultSummary.counters().propertiesSet() > 0;
            });
        }
    }

    public static void main(String[] args) {
        DoctorDao dao = new DoctorDao();
//        Doctor doctor = new Doctor ("Dr.78090", "HungHung", "1234579", "Rang Ham Mat");
//        System.out.println("Ket qua tra ve: " + dao.addDoctor(doctor));
//                dao.getNoOfDoctorsBySpeciality("Community Medicine")
//                .forEach((k,v) -> System.out.println(k + ": " + v));
                dao.lisDocTorsBySpeciality("Community medicine").forEach(System.out::println );
//        System.out.println(updateDiagnosis("PT005","DR.011", "Lalalala"));
    }

}
