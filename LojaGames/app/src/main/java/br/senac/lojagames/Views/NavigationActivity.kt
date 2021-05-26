package br.senac.lojagames.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
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
        val frag = CatalogoFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.fragContainer, frag).commit()

        //Troca Fragmentos
        b.navigationView.setNavigationItemSelectedListener {
            b.drawerLayout.closeDrawers()

            when (it.itemId) {
                R.id.catalogo -> {
                    val frag = CatalogoFragment.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.fragContainer, frag).commit()

                    true
                }

                else -> false
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        toggle?.let {
            return it.onOptionsItemSelected(item)
        }
        return false
    }
}