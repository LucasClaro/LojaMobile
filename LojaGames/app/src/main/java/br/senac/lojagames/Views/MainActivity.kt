package br.senac.lojagames.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.senac.lojagames.Model.Produto
import br.senac.lojagames.R
import br.senac.lojagames.Services.ProdutoService
import br.senac.lojagames.databinding.ActivityMainBinding
import br.senac.lojagames.databinding.GameCardBinding
import com.google.android.material.snackbar.Snackbar
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var b : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
    }

    override fun onResume() {
        super.onResume()

        atualizarProdutos()
    }

    fun atualizarUI(listaProdutos : List<Produto>?) {
        b.container.removeAllViews()

        listaProdutos?.forEach {
            val cardBinding = GameCardBinding.inflate(layoutInflater)

            cardBinding.GameName.text = it.nome
            cardBinding.GamePrice.text = it.preco.toString()

            b.container.addView(cardBinding.root)
        }
    }

    fun atualizarProdutos() {

        val http = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://murmuring-wave-61983.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(http)
            .build()

        val service = retrofit.create(ProdutoService::class.java)

        val call = service.list()

        val callback = object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful) {
                    atualizarUI(response.body())
                }
                else {
                    Snackbar
                        .make(b.container, R.string.CallbackErrorResponse, Snackbar.LENGTH_LONG)
                        .show()

                    Log.e("ERRO", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                Snackbar
                    .make(b.container, R.string.CallbackErrorConection, Snackbar.LENGTH_LONG)
                    .show()

                Log.e("ERRO", "Falha ao chamar o serviço", t)
            }
        }

        call.enqueue(callback)
    }

}