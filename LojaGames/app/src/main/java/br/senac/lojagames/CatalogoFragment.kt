package br.senac.lojagames

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.senac.lojagames.Model.Produto
import br.senac.lojagames.Services.ProdutoService
import br.senac.lojagames.databinding.FragmentCatalogoBinding
import br.senac.lojagames.databinding.GameCardBinding
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CatalogoFragment : Fragment() {

    lateinit var b : FragmentCatalogoBinding
    var filtroPesquisa : String? = null
    var filtrosCategorias  = arrayListOf<String>()
    var produtos = arrayListOf<Produto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        filtroPesquisa = getArguments()?.getString("Pesquisa");
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        b = FragmentCatalogoBinding.inflate(inflater)

        var categorias = arrayListOf("MetroidVania", "Plataforma", "Puzzle", "Shooter", "Rogue Like")
        categorias.forEach {
            val chip = Chip(this.context)

            when (it) {
                "MetroidVania" -> chip.setText(R.string.ChipMetroidVania)
                "Plataforma" -> chip.setText(R.string.ChipPlataforma)
                "Puzzle" -> chip.setText(R.string.ChipPuzzle)
                "Shooter" -> chip.setText(R.string.ChipShooter)
                "Rogue Like" -> chip.setText(R.string.ChipRogueLike)
            }
            chip.isCheckable = true

            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    filtrosCategorias.add(it)
                }
                else {
                    filtrosCategorias.remove(it)
                }
                atualizarUI()

            }

            b.chipGroup.addView(chip)
        }


        return b.root
    }

    override fun onResume() {
        super.onResume()

        atualizarProdutos()
    }

    fun atualizarUI() {
        b.container.removeAllViews()

        produtos?.forEach {

            if (filtrosCategorias.count() <= 0 || filtrosCategorias.contains(it.categoria)) {
                if (filtroPesquisa != null) {
                    if (it.nome.contains(filtroPesquisa!!, true)) {
                        val cardBinding = GameCardBinding.inflate(layoutInflater)

                        cardBinding.GameName.text = it.nome
                        cardBinding.GamePrice.text = it.preco.toString()

                        b.container.addView(cardBinding.root)
                    }
                }
                else {
                    val cardBinding = GameCardBinding.inflate(layoutInflater)

                    cardBinding.GameName.text = it.nome
                    cardBinding.GamePrice.text = it.preco.toString()

                    b.container.addView(cardBinding.root)
                }
            }

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
                    produtos = response.body() as ArrayList<Produto>
                    atualizarUI()
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

    companion object {
        @JvmStatic
//        fun newInstance() = CatalogoFragment()

        fun newInstance(TextoPesquisa: String?): CatalogoFragment {
            val myFragment = CatalogoFragment()
            val args = Bundle()
            args.putString("Pesquisa", TextoPesquisa)
            myFragment.setArguments(args)
            return myFragment
        }

    }


}