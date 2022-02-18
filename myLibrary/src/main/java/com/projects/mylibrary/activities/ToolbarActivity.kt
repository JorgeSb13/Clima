package com.projects.mylibrary.activities

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.projects.mylibrary.interfaces.IToolbar

open class ToolbarActivity : AppCompatActivity(), IToolbar {

    protected var toolbar: Toolbar? = null

    override fun toolbarToLoad(toolbar: Toolbar?) {
        this.toolbar = toolbar
        this.toolbar?.let {
            title = ""
            setSupportActionBar(this.toolbar)
        }
    }

    override fun enableHomeDisplay(value: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(value)
    }

}