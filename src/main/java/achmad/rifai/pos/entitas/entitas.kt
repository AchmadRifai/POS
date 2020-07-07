/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package achmad.rifai.pos.entitas

import achmad.rifai.pos.utils.Uang
import java.sql.Date
import java.sql.Timestamp

/*

  @author AmMas
  Created on Jun 30, 2020
*/
// umum
data class Perusahaan(var nm:String,var cab:List<Cabang>)
data class Cabang(var nomor:Int,var tlp:String,var almt:String,var pusat:Boolean,var ini:Boolean)
data class Jabatan(var kode:Int,var nm:String,var gaji:Uang,var minim:Int,var maxim:Int)
data class Pegawai(var uid:String,var jab:Jabatan,var pwd:String,var nm:String,var almt:String,var tlp:String,
                   var masuk:Boolean,var logs:List<Jejak>,var abs:List<Absen>,var trans:List<Transaksi>)
data class Jejak(var waktu:Timestamp,var dari:String,var ket:String)
data class Absen(var tgl:Date,var dari:String,var ket:String)
//transaksi
data class Register(var kode:String,var nm:String,var ket:String,var jum:Uang,var simpan:Boolean)
data class Transaksi(var nota:String,var ttd:String,var ke:Register,var dari:Register,var jum:Uang,var tgl:Date,
                     var waktu:Timestamp,var ket:String)
data class Hutang(var kode:Int,var jum:Uang,var tgl:Date,var ket:String,var catat:Transaksi,var cicil:List<Cicilan>)
data class Piutang(var kode:Int,var jum:Uang,var tgl:Date,var ket:String,var catat:Transaksi,var cicil:List<Dicicil>)
data class Dicicil(var catat:Transaksi)
data class Cicilan(var catat:Transaksi)
//gudang
data class Suplier(var kode:String,var nm:String,var almt:String,var sal:List<Sales>,var dri:List<Shipper>)
data class Sales(var kode:String,var nm:String,var tlp:String,var almt:String)
data class Shipper(var kode:String,var nm:String,var tlp:String,var almt:String)
data class Kategori(var kode:String,var nm:String,var bars:List<Barang>)
data class Barang(var kode:String,var nm:String,var bs:Boolean,var sat:List<SatuanBarang>)
data class SatuanBarang(var barkode:String,var nm:String,var jual:Uang,var beli:Uang,var stok:Long,var cap:Int,
var bar:Barang?)
data class Pasok(var kode:String,var waktu:Timestamp,var catat:Transaksi,var jum:Uang,var sup:Suplier,var hut:Hutang?
                 ,var item:List<ItemPasok>)
data class ItemPasok(var brg:SatuanBarang,var qty:Int,var beli:Uang)
data class PasokBS(var kode:String,var waktu:Timestamp,var catat:Transaksi,var jum:Uang,var sup:Suplier,
                   var pas:List<ItemPasokBS>,var back:List<ItemBackBS>)
data class ItemPasokBS(var brg:SatuanBarang,var qty:Int,var beli:Uang)
data class ItemBackBS(var brg:SatuanBarang,var qty:Int,var beli:Uang)
data class Retur(var kode:String,var waktu:Timestamp,var catat:Transaksi,var jum:Uang,var sup:Suplier,
                 var item:List<ItemRetur>)
data class ItemRetur(var brg:SatuanBarang,var qty:Int,var back:Uang)
//jual
data class Pelanggan(var kode:Int,var nm:String,var almt:String,var tlp:String,var pot:Uang)
data class Jual(var nota:String,var tgl:Date,var waktu:Timestamp,var tot:Uang,var byr:Uang,var kbl:Uang,var pot:Uang,
                var catat:Transaksi,var pel:Pelanggan,var piu:Piutang?,var nm:String,var ket:String,var item:List<ItemJual>)
data class ItemJual(var brg:SatuanBarang,var qty:Int,var hrg:Uang)
