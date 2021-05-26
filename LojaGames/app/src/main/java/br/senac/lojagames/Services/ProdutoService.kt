package br.senac.lojagames.Services

import br.senac.lojagames.Model.Produto
import retrofit2.Call
import retrofit2.http.GET

interface ProdutoService {

    @GET("/produtos")
    fun list(): Call<List<Produto>>

}