package br.senac.lojagames.Model

data class Produto(
    var id : Int,
    var nome : String,
    var preco : Double,
    var categoria : String,
    var desconto : Int
)
