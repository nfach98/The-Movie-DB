package com.nf98.moviecatalogue.app.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.nf98.moviecatalogue.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(nav_main)
        bnv_main.setupWithNavController(navController)

        iconSetting.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if(v.id == R.id.iconSetting) startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }
}
