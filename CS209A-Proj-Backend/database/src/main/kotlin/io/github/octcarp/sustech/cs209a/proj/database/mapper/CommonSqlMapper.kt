package io.github.octcarp.sustech.cs209a.proj.database.mapper

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface CommonSqlMapper {

    @Select("SELECT 1")
    fun testConnection(): Int

    @Update("SET session_replication_role = 'replica';")
    fun disableAllTriggers()

    @Update("SET session_replication_role = 'origin';")
    fun enableAllTriggers()


//    @Update(
//        """
//        ALTER TABLE Users DISABLE TRIGGER ALL;
//        ALTER TABLE Questions DISABLE TRIGGER ALL;
//        ALTER TABLE Answers DISABLE TRIGGER ALL;
//        ALTER TABLE Comments DISABLE TRIGGER ALL;
//        ALTER TABLE Tags DISABLE TRIGGER ALL;
//        ALTER TABLE Topics DISABLE TRIGGER ALL;
//        ALTER TABLE Post_Topics DISABLE TRIGGER ALL;
//        ALTER TABLE Bugs DISABLE TRIGGER ALL;
//        ALTER TABLE Post_Bugs DISABLE TRIGGER ALL;
//    """
//    )
//    fun disableAllTriggers()
//
//    @Update(
//        """
//            ALTER TABLE Users ENABLE TRIGGER ALL;
//            ALTER TABLE Questions ENABLE TRIGGER ALL;
//            ALTER TABLE Answers ENABLE TRIGGER ALL;
//            ALTER TABLE Comments ENABLE TRIGGER ALL;
//            ALTER TABLE Tags ENABLE TRIGGER ALL;
//            ALTER TABLE Topics ENABLE TRIGGER ALL;
//            ALTER TABLE Post_Topics ENABLE TRIGGER ALL;
//            ALTER TABLE Bugs ENABLE TRIGGER ALL;
//            ALTER TABLE Post_Bugs ENABLE TRIGGER ALL;
//            """
//    )
//    fun enableAllTriggers()
}