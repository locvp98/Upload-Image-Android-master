package com.hoc.uploadimage

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.hoc.uploadimage.imagelist.ImageListFragment
import com.hoc.uploadimage.upload.UploadFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val sectionsPagerAdapter = SectionsPagerAdapter(
            supportFragmentManager,
            listOf(UploadFragment(), ImageListFragment()),
            listOf("Upload", "Images")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        container.adapter = sectionsPagerAdapter
        tabLayout.setupWithViewPager(container)
    }

    class SectionsPagerAdapter(
            fm: FragmentManager,
            val fragments: List<Fragment>,
            val titles: List<CharSequence?>
    ) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int) = fragments[position]
        override fun getCount() = fragments.size
        override fun getPageTitle(position: Int) = titles[position]
    }
}

