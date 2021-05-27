package br.senac.lojagames.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import br.senac.lojagames.CatalogoFragment
import br.senac.lojagames.R
import br.senac.lojagames.databinding.ActivityNavigationBinding

class NavigationActivity : AppCompatActivity() {

    lateinit var b : ActivityNavigationBinding
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(b.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Gerencia Toggle
        toggle = ActionBarDrawerToggle(this, b.drawerLayout, R.string.AbrirMenu, R.string.FecharMenu)
        b.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //Define o frag default
        val frag = CatalogoFragment.newInstance(null)
        supportFragmentManager.beginTransaction().replace(R.id.fragContainer, frag).commit()

        //Troca Fragmentos
        b.navigationView.setNavigationItemSelectedListener {
            b.drawerLayout.closeDrawers()

            when (it.itemId) {
                R.id.catalogo -> {
                    val frag = CatalogoFragment.newInstance(null)
                    supportFragmentManager
                            .beginTransaction()
                            .replace(b.fragContainer.id, frag)
                            .commit()

                    true
                }

                else -> false
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

                override fun onQueryTextSubmit(query: String?): Boolean {

                    query?.let {
                        val frag = CatalogoFragment.newInstance(query)
                        supportFragmentManager
                                .beginTransaction()
                                .replace(b.fragContainer.id, frag)
                                .commit()
                    } ?: run {
                        val frag = CatalogoFragment.newInstance(null)
                        supportFragmentManager
                                .beginTransaction()
                                .replace(b.fragContainer.id, frag)
                                .commit()
                    }

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

            })
            searchView.setOnCloseListener(object : SearchView.OnCloseListener {
                override fun onClose(): Boolean {

                    val frag = CatalogoFragment.newInstance(null)
                    supportFragmentManager
                            .beginTransaction()
                            .replace(b.fragContainer.id, frag)
                            .commit()

                    return true
                }

            })

        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){

        }

        return super.onOptionsItemSelected(item)
    }
}