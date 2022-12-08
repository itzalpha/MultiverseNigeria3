package com.example.multiversenigeria3.ui.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import com.example.multiversenigeria3.R
import com.example.multiversenigeria3.databinding.FragmentSettingsBinding
import com.example.multiversenigeria3.general.AdvertiseYourApp
import com.example.multiversenigeria3.general.SubscribeForTheMonth
import com.example.multiversenigeria3.login.SplashActivity
import com.example.multiversenigeria3.me.*
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel

    private var _binding : FragmentSettingsBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)


        return binding.root

   }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)


        binding.country.setOnClickListener {
         Toast.makeText(context , "Presently Available for Nigeria" ,Toast.LENGTH_SHORT).show()
        }

        binding.advertiseYourApp.setOnClickListener {
            startActivity(Intent(context ,  AdvertiseYourApp::class.java))
        }

        binding.buyUsACoffee.setOnClickListener {
            startActivity(Intent(context , SubscribeForTheMonth::class.java))
        }

        binding.reportaproblem.setOnClickListener {
            startActivity(Intent(context ,  ReportAProblem::class.java))
        }

        binding.sendfeedback.setOnClickListener {
            startActivity(Intent(context ,  SendFeedback::class.java))
        }

        binding.joinOurTeam.setOnClickListener {
            startActivity(Intent(context ,  JoinOurTeam::class.java))
        }

        binding.shareApp.setOnClickListener {
            ShareCompat.IntentBuilder.from(context as Activity)
                .setType("text/plain")
                .setChooserTitle("Skill Verse")
                .setText("http://play.google.com/store/apps/details?id=" + (context as Activity).applicationContext.packageName)

                .startChooser()
        }

        binding.rateApp.setOnClickListener {
            val uri =
                Uri.parse("https://play.google.com/store/apps/details?id=" + (context as Activity).applicationContext.packageName)
            val i = Intent(Intent.ACTION_VIEW, uri)
            startActivity(i)
            try {
                startActivity(i)
            } catch (e: Exception) {
                Toast.makeText(
                    context, """Unable to Open
                ${e.message}""", Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.aboutAppStoreVerse.setOnClickListener {
            startActivity(Intent(context ,  AboutShareVerse::class.java))
        }

        binding.verseCityTechCompany.setOnClickListener {
            startActivity(Intent(context ,  Multiverse::class.java))
        }

        binding.privacypolicy.setOnClickListener {
            startActivity(Intent(context ,  PrivacyPolicy::class.java))
        }

        binding.communityguidelines.setOnClickListener {
            startActivity(Intent(context ,  CommunityGuidelines::class.java))
        }

        binding.logout.setOnClickListener {
            val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
            builder?.setTitle(R.string.app_name)
            builder?.setIcon(R.mipmap.ic_launcher)
            builder?.setMessage("Do you want to Log out")
                ?.setCancelable(false)
                ?.setPositiveButton("Yes") { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(context, SplashActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT)
                        .show()
                }?.setNegativeButton(
                    "No"
                ) { dialogInterface, _ -> dialogInterface.cancel() }
            val alert = builder?.create()
            alert?.show()


        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}