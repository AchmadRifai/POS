package achmad.rifai.pos.entitas.dao

import achmad.rifai.pos.entitas.*
import achmad.rifai.pos.utils.Db
import achmad.rifai.pos.utils.Params
import achmad.rifai.pos.utils.Uang
import achmad.rifai.pos.utils.Work
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.sql.Types
import java.time.LocalDate
import java.time.LocalDateTime

class DAOPerusahaan(val d:Db):DAO<Perusahaan,String>{
    override fun create() {
        d.exec("create table if not exists perusahaan(nm varchar(40)primary key)")
        d.exec("create table if not exists cabang(nomor int primary key,tlp varchar(20)not null,almt text not null," +
                "pusat boolean not null,ini boolean not null,comp varchar(40)not null)")
        d.exec("alter table cabang add foreign key(comp)references perusahaan(nm)on update cascade on delete cascade")
    }

    override fun drop() {
        d.exec("drop table cabang")
        d.exec("drop table perusahaan")
    }

    override fun clean() {
        d.exec("delete from cabang")
        d.exec("delete from perusahaan")
    }

    override fun one(w: String?): Perusahaan {
        val p=Perusahaan("", emptyList())
        for (v in d.hasil("select nm from perusahaan where nm=?", Params(w!!,Types.VARCHAR))){
            val nm=v["nm"]!!.`val`as String
            p.nm=nm
            p.cab=cabange(nm)
        }
        return p
    }

    override fun all(): MutableList<Perusahaan> {
        val l=mutableListOf<Perusahaan>()
        val r=d.hasil("select nm from perusahaan")
        while (r.next()){
            val nm=r.getString("nm")
            val p=Perusahaan(nm,cabange(nm))
            l.add(p)
        }
        r.close()
        return l
    }

    private fun cabange(nm: String?): List<Cabang> {
        val l= emptyList<Cabang>()
        for (v in d.hasil("select nomor,tlp,almt,pusat,ini from cabang where comp=?",Params(nm!!,Types.VARCHAR))) {
            val c=Cabang(v["nomor"]!!.`val` as Int,v["tlp"]!!.`val`as String,v["almt"]!!.`val`as String,
            v["pusat"]!!.`val`as Boolean,v["ini"]!!.`val`as Boolean)
            l.plus(c)
        }
        return l
    }

    override fun insert(v: Perusahaan?) {
        d.exec("insert into perusahaan values(?)", Params(v!!.nm, Types.VARCHAR))
        fillCabang(v)
    }

    override fun update(w: String?, v: Perusahaan?) {
        d.exec("delete from cabang where comp=?", Params(w!!,Types.VARCHAR))
        d.exec("update perusahaan set nm=? where nm=?", Params(v!!.nm,Types.VARCHAR), Params(w, Types.VARCHAR))
        fillCabang(v)
    }

    private fun fillCabang(v: Perusahaan) {
        for (i in v.cab) {
            d.exec("insert into cabang values(?,?,?,?,?,?)", Params(i.nomor, Types.INTEGER), Params(i.tlp, Types.VARCHAR),
            Params(i.almt, Types.VARCHAR), Params(i.pusat, Types.BOOLEAN), Params(i.ini, Types.BOOLEAN),
                    Params(v.nm, Types.VARCHAR))
        }
    }

    override fun delete(w: String?) {
        d.exec("delete from cabang where comp=?", Params(w!!,Types.VARCHAR))
        d.exec("delete from perusahaan where comp=?", Params(w,Types.VARCHAR))
    }
}

class DAOJabatan(val d:Db):DAO<Jabatan,Int> {
    override fun create()=d.exec("create table if not exists jabatan(kode int primary key,nm text not null," +
            "gaji decimal(20,20)not null,minim int not null,maxim int not null)")

    override fun drop()=d.exec("drop table jabatan")

    override fun clean()=d.exec("delete from jabatan")

    override fun one(w: Int?): Jabatan {
        val j=Jabatan(w!!,"",Uang(BigDecimal.ZERO),0,0)
        for (i in d.hasil("select nm,gaji,minim,maxim from jabatan where kode=?",Params(w,Types.INTEGER))){
            j.gaji= Uang(i["gaji"]!!.`val`as BigDecimal)
            j.maxim=i["maxim"]!!.`val`as Int
            j.minim=i["minim"]!!.`val`as Int
            j.nm=i["nm"]!!.`val`as String
        }
        return j
    }

    override fun all(): MutableList<Jabatan> {
        val l= mutableListOf<Jabatan>()
        val r=d.hasil("select*from jabatan")
        while (r.next()) {
            val j=Jabatan(r.getInt("kode"),r.getString("nm"), Uang(r.getBigDecimal("gaji")),r.getInt("minim"),
            r.getInt("maxim"))
            l.add(j)
        }
        r.close()
        return l
    }

    override fun insert(v: Jabatan?)=d.exec("insert into jabatan values(?,?,?,?,?)",Params(v!!.kode,Types.INTEGER),
            Params(v.nm,Types.VARCHAR),Params(v.gaji.`val`,Types.DECIMAL), Params(v.minim,Types.INTEGER),
            Params(v.maxim,Types.INTEGER))

    override fun update(w: Int?, v: Jabatan?)=d.exec("update jabatan set kode=?,nm=?,gaji=?,minim=?,maxim=? where kode=?",
            Params(v!!.kode,Types.INTEGER),Params(v.nm,Types.VARCHAR), Params(v.gaji.`val`,Types.DECIMAL),
            Params(v.minim,Types.INTEGER),Params(v.maxim,Types.INTEGER), Params(w!!,Types.INTEGER))

    override fun delete(w: Int?)=d.exec("delete from jabatan where kode=?", Params(w!!,Types.INTEGER))
}

class DAORegister(val d:Db):DAO<Register,String> {
    override fun create()=d.exec("create table if not exists register(kode varchar(20)primary key,nm text not null," +
            "ket text not null,jum decimal(20,20)not null,simpan boolean not null)")

    override fun drop()=d.exec("drop table register")

    override fun clean()=d.exec("delete from register")

    override fun one(w: String?): Register {
        val g=Register(w!!,"","", Uang(BigDecimal.ZERO),false)
        for (v in d.hasil("select nm,ket,jum,simpan from register where kode=?",Params(w,Types.VARCHAR))){
            g.jum= Uang(v["jum"]!!.`val`as BigDecimal)
            g.ket=v["ket"]!!.`val`as String
            g.nm=v["nm"]!!.`val`as String
            g.simpan=v["simpan"]!!.`val`as Boolean
        }
        return g
    }

    override fun all(): MutableList<Register> {
        val l= mutableListOf<Register>()
        val r=d.hasil("select*from register")
        while (r.next()) {
            val g=Register(r.getString("kode"),r.getString("nm"),r.getString("ket"), Uang(r.getBigDecimal("jum")),
            r.getBoolean("simpan"))
            l.add(g)
        }
        r.close()
        return l
    }

    override fun insert(v: Register?)=d.exec("insert into register values(?,?,?,?,?)", Params(v!!.kode,Types.VARCHAR),
    Params(v.nm,Types.VARCHAR), Params(v.ket,Types.VARCHAR),Params(v.jum.`val`,Types.DECIMAL),
            Params(v.simpan,Types.BOOLEAN))

    override fun update(w: String?, v: Register?)=d.exec("update set register kode=?,nm=?,ket=?,jum=?,simpan=? where " +
            "kode=?", Params(v!!.kode,Types.VARCHAR), Params(v.nm,Types.VARCHAR), Params(v.ket,Types.VARCHAR),
    Params(v.jum.`val`,Types.DECIMAL), Params(v.simpan,Types.BOOLEAN), Params(w!!,Types.VARCHAR))

    override fun delete(w: String?)=d.exec("delete from register where kode=?", Params(w!!,Types.VARCHAR))
}

class DAOPegawai(val d:Db):DAO<Pegawai,String> {
    override fun create() {
        d.exec("create table if not exists pegawai(uid varchar(20)primary key,jab int not null,pwd text not null," +
                "nm text not null,almt text not null,tlp varchar(20)not null,masuk boolean not null)")
        d.exec("create table if not exists jejak(waktu timestamp not null,dari varchar(20)not null,ket text not null)")
        d.exec("create table if not exists absen(tgl date not null,dari varchar(20)not null,ket text not null)")
        d.exec("alter table pegawai add foreign key(jab)references jabatan(kode)on update cascade on delete cascade")
        d.exec("alter table jejak add foreign key(dari)references pegawai(uid)on update cascade on delete cascade")
        d.exec("alter table absen add foreign key(dari)references pegawai(uid)on update cascade on delete cascade")
    }

    override fun drop() {
        d.exec("drop table absen")
        d.exec("drop table jejak")
        d.exec("drop table pegawai")
    }

    override fun clean() {
        d.exec("delete from absen")
        d.exec("delete from jejak")
        d.exec("delete from pegawai")
    }

    override fun one(w: String?): Pegawai {
        val p=Pegawai(w!!,Jabatan(0,"",Uang(BigDecimal.ZERO),0,0),"","","","",false,emptyList(),emptyList(),emptyList())
        val dao=DAOJabatan(d)
        for (i in d.hasil("select jab,pwd,nm,almt,tlp,masuk from pegawai where uid=?",Params(w,Types.VARCHAR))){
            p.jab=dao.one(i["jab"]!!.`val`as Int)
            p.almt=i["almt"]!!.`val`as String
            p.masuk=i["masuk"]!!.`val`as Boolean
            p.nm=i["nm"]!!.`val`as String
            p.pwd=i["pwd"]!!.`val`as String
            p.tlp=i["tlp"]!!.`val`as String
            p.logs=allJejak(w)
            p.abs=allAbsen(w)
            p.trans=allTrans(w)
        }
        return p
    }

    private fun allTrans(w: String): List<Transaksi> {
        val l= emptyList<Transaksi>()
        val dao=DAOTransaksi(d)
        for (i in d.hasil("select nota from transaksi where ttd=?",Params(w,Types.VARCHAR))){
            l.plus(dao.one(i["nota"]!!.`val`as String))
        }
        return l
    }

    private fun allAbsen(w: String): List<Absen> {
        val l= emptyList<Absen>()
        for (i in d.hasil("select*from absen where dari=?",Params(w,Types.VARCHAR))){
            val a=Absen(i["tgl"]!!.`val`as Date,w,i["ket"]!!.`val`as String)
            l.plus(a)
        }
        return l
    }

    private fun allJejak(w: String): List<Jejak> {
        val l= emptyList<Jejak>()
        for (i in d.hasil("select*from jejak where dari=?",Params(w,Types.VARCHAR))){
            val j=Jejak(i["waktu"]!!.`val`as Timestamp,w,i["ket"]!!.`val`as String)
            l.plus(j)
        }
        return l
    }

    override fun all(): MutableList<Pegawai> {
        val l= mutableListOf<Pegawai>()
        val r=d.hasil("select uid from pegawai")
        while (r.next())l.add(one(r.getString("uid")))
        r.close()
        return l
    }

    override fun insert(v: Pegawai?) {
        d.exec("insert into pegawai values(?,?,?,?,?,?,?)", Params(v!!.uid,Types.VARCHAR),
                Params(v.jab.kode,Types.INTEGER),Params(v.pwd,Types.VARCHAR), Params(v.nm,Types.VARCHAR),
                Params(v.almt,Types.VARCHAR), Params(v.tlp,Types.VARCHAR),Params(v.masuk,Types.BOOLEAN))
        fillPegawai(v)
    }

    override fun update(w: String?, v: Pegawai?) {
        d.exec("delete from jejak where dari=?", Params(w!!,Types.VARCHAR))
        d.exec("delete from absen where dari=?", Params(w,Types.VARCHAR))
        d.exec("update pegawai set uid=?,jab=?,pwd=?,nm=?,almt=?,tlp=?,masuk=? where uid=?",
                Params(v!!.uid,Types.VARCHAR),
        Params(v.jab.kode,Types.INTEGER),Params(v.pwd,Types.VARCHAR), Params(v.nm,Types.VARCHAR),
                Params(v.almt,Types.VARCHAR), Params(v.tlp,Types.VARCHAR), Params(v.masuk,Types.BOOLEAN),
                Params(w,Types.VARCHAR))
        fillPegawai(v)
    }

    private fun fillPegawai(v: Pegawai) {
        for (i in v.abs)d.exec("insert into absen values(?,?,?)", Params(i.tgl,Types.DATE),
                Params(i.dari,Types.VARCHAR),
        Params(i.ket,Types.VARCHAR))
        for (i in v.logs)d.exec("insert into jejak values(?,?,?)", Params(i.waktu,Types.TIMESTAMP),
        Params(i.dari,Types.VARCHAR), Params(i.ket,Types.VARCHAR))
    }

    override fun delete(w: String?) {
        d.exec("delete from jejak where dari=?", Params(w!!,Types.VARCHAR))
        d.exec("delete from absen where dari=?", Params(w,Types.VARCHAR))
        d.exec("delete from pegawai where uid=?", Params(w,Types.VARCHAR))
    }
}

class DAOTransaksi(val d:Db):DAO<Transaksi,String> {
    override fun create() {
        d.exec("create table if not exists transaksi(nota varchar(40)primary key,ttd varchar(20)not null," +
                "ke varchar(20)not null,dari varchar(20)not null,jum decimal(20,20)not null,tgl date not null," +
                "waktu timestamp not null,ket text not null)")
        d.exec("alter table transaksi add foreign key(ttd)references pegawai(uid)on update cascade on delete cascade")
        d.exec("alter table transaksi add foreign key(ke)references register(kode)on update cascade on delete cascade")
        d.exec("alter table transaksi add foreign key(dari)references register(kode)on update cascade on delete cascade")
    }

    override fun drop()=d.exec("drop table transaksi")

    override fun clean()=d.exec("delete from transaksi")

    override fun one(w: String?): Transaksi {
        val dao=DAORegister(d)
        val t=Transaksi(w!!,"",dao.one(""),dao.one(""),Uang(BigDecimal.ZERO),Date.valueOf(LocalDate.now()),
                Timestamp.valueOf(LocalDateTime.now()),"")
        for (i in d.hasil("select ttd,ke,dari,jum,tgl,waktu,ket from transaksi where nota=?",Params(w,Types.VARCHAR))) {
            t.dari=dao.one(i["dari"]!!.`val`.toString())
            t.jum= Uang(i["jum"]!!.`val`as BigDecimal)
            t.ke=dao.one(i["ke"]!!.`val`.toString())
            t.ket=i["ket"]!!.`val`.toString()
            t.tgl=i["tgl"]!!.`val`as Date
            t.ttd=i["ttd"]!!.`val`.toString()
            t.waktu=i["waktu"]!!.`val`as Timestamp
        }
        return t
    }

    override fun all(): MutableList<Transaksi> {
        val l= mutableListOf<Transaksi>()
        val r=d.hasil("select nota from transaksi order by waktu desc")
        while (r.next())l.add(one(r.getString("nota")))
        r.close()
        return l
    }

    override fun insert(v: Transaksi?) {
        d.exec("insert into transaksi values(?,?,?,?,?,?,?,?)", Params(v!!.nota,Types.VARCHAR), Params(v.ttd,Types.VARCHAR),
        Params(v.ke.kode,Types.VARCHAR), Params(v.dari.kode,Types.VARCHAR),Params(v.jum.`val`,Types.VARCHAR), Params(v.tgl,Types.DATE),
        Params(v.waktu,Types.TIMESTAMP), Params(v.ket,Types.VARCHAR))
        Thread{
            tataRegister()
        }.start()
    }

    override fun update(w: String?, v: Transaksi?){
        d.exec("update transaksi set nota=?,ttd=?,ke=?,dari=?,jum=?,tgl=?," +
                "waktu=?,ket=? where nota=?", Params(v!!.nota,Types.VARCHAR), Params(v.ttd,Types.VARCHAR),
                Params(v.ke.kode,Types.VARCHAR), Params(v.dari.kode,Types.VARCHAR),Params(v.jum.`val`,Types.DECIMAL),
                Params(v.tgl,Types.DATE),Params(v.waktu,Types.TIMESTAMP), Params(v.ket,Types.VARCHAR), Params(w!!,Types.VARCHAR))
        Thread{
            tataRegister()
        }.start()
    }

    override fun delete(w: String?){
        d.exec("delete from transaksi where nota=?", Params(w!!,Types.VARCHAR))
        Thread{
            tataRegister()
        }.start()
    }
}

class DAOHutang(val d:Db):DAO<Hutang,Int>{
    override fun create() {
        d.exec("create table hutang(kode int primary key AUTO_INCREMENT,jum decimal(20,20)not null,tgl date not null," +
                "ket text not null,catat varchar(40)not null)")
        d.exec("create table cicilan(hut int not null,catat varchar(40)not null)")
        d.exec("alter table hutang add foreign key(catat)references transaksi(nota)on update cascade on delete cascade")
        d.exec("alter table cicilan add foreign key(hut)references hutang(kode)on update cascade on delete cascade")
        d.exec("alter table cicilan add foreign key(catat)references transaksi(nota)on update cascade on delete cascade")
    }

    override fun drop() {
        d.exec("drop table cicilan")
        d.exec("drop table hutang")
    }

    override fun clean() {
        d.exec("delete from cicilan")
        d.exec("delete from hutang")
    }

    override fun one(w: Int?): Hutang {
        TODO("Not yet implemented")
    }

    override fun all(): MutableList<Hutang> {
        TODO("Not yet implemented")
    }

    override fun insert(v: Hutang?) {
        TODO("Not yet implemented")
    }

    override fun update(w: Int?, v: Hutang?) {
        TODO("Not yet implemented")
    }

    override fun delete(w: Int?) {
        TODO("Not yet implemented")
    }
}

fun tataRegister(){
    val d=Work.loadDb()
    val ls=allRegis(d)
    for (reg in ls) {
        var jum=BigDecimal.ZERO
        for (i in d.hasil("select jum from transaksi where ke=?", Params(reg,Types.VARCHAR)))
            jum=jum.add(i["jum"]!!.`val`as BigDecimal)
        for (i in d.hasil("select jum from transaksi where dari=?", Params(reg,Types.VARCHAR)))
            jum=jum.subtract(i["jum"]!!.`val`as BigDecimal)
        d.exec("update register set jum=? where kode=?", Params(jum,Types.DECIMAL), Params(reg,Types.VARCHAR))
    }
    d.close()
}

fun allRegis(d: Db?): List<String> {
    val l= emptyList<String>()
    val r=d!!.hasil("select kode from register where simpan")
    while (r.next())l.plus(r.getString("kode"))
    r.close()
    return l
}
