/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package achmad.rifai.pos.entitas

import achmad.rifai.pos.utils.Db
import achmad.rifai.pos.utils.Params
import java.sql.SQLException
import java.sql.Types
import kotlin.collections.emptyList as emptyList

/*

  @author AmMas
  Created on Jul 1, 2020
*/
class DAOPerusahaan(val d:Db):DAO<Perusahaan, String> {
    override fun create() {
        try {
            d.exec("create table perusahaan(nm varchar(20)primary key)")
            d.exec("create table cabang(nomor int primary key auto_increment,comp varchar(20)not null," +
                    "tlp char(20)not null,almt text not null,pusat boolean not null, ini boolean not null)")
            d.exec("alter table cabang add foreign key(comp)references perusahaan(nm)on update cascade on" +
                    " delete cascade")
        }catch (e:SQLException){
            throw e
        }
    }

    override fun drop() {
        try {
            d.exec("drop table cabang")
            d.exec("drop table perusahaan")
        } catch (e:SQLException) {
            throw e
        }
    }

    override fun one(id: String): Perusahaan? {
        var g:Perusahaan?=null
        try {
            val r=d.hasil("select nm from perusahaan")
            if(r.next()) {
                val l= emptyList<Cabang>()
                g=Perusahaan(r.getString("nm"), emptyList())
                for(i in d.hasil("select*from cabang where comp=?", Params(g.nm, Types.VARCHAR))){
                    val c=Cabang(r.getInt("nomor"),r.getString("tlp"),r.getString("almt"),r.getBoolean("pusat"),
                    r.getBoolean("ini"))
                    l.plus(c)
                };g.cab=l
            };r.close()
        } catch (e:SQLException){
            throw e
        };return g
    }
}

interface DAO<T,W>{
    fun create()
    fun drop()
    fun one(id:W):T?
}