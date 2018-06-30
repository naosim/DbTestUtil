package com.naosim.dbtestutil

import com.naosim.dbtestutil.db.TableInsertEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

@Unroll
@SpringBootTest
@TestConfiguration
class DbTestComponentSpec extends Specification {
    @Autowired
    DbTestComponent sut;

    def setup() {
        try {
            sut.exec("CREATE TABLE user (name TEXT, date DATE)")
        } catch (Exception e) {

        }

    }

    def cleanup() {
        sut.exec("DROP TABLE user")
    }

    def "test"() {


        when:
        sut.insert(TableInsertEvent.createFromMap(
                "user",
                [
                        name: "hoge",
                        date: LocalDate.of(2018, 1, 1)
                ]
        ))
        def act = sut.selectAll("user")

        then:
        act == [[date:1514732400000, name:"hoge"]]

    }
}
