package com.sevenlearn.nike.feature.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.NikeFragment
import com.sevenlearn.nike.feature.auth.AuthActivity
import com.sevenlearn.nike.feature.favorite.FavoriteProductActivity
import com.sevenlearn.nike.feature.order.OrderActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment: NikeFragment() {

    val viewModel:ProfileViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteProductsBtn.setOnClickListener {
            startActivity(Intent(requireContext(),FavoriteProductActivity::class.java))
        }

        orderHistoryBtn.setOnClickListener {
            startActivity(Intent(requireContext(),OrderActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        checkOutState()
    }

    private fun checkOutState() {
        if(viewModel.isSignedIn){
            authBtn.text = getString(R.string.signOut)
            authBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_sign_out,0)
            usernameTv.text =viewModel.username
            authBtn.setOnClickListener{
                viewModel.signOut()
                checkOutState()
            }
        }else{
            authBtn.text = getString(R.string.signIn)
            authBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_sign_in,0)
            usernameTv.text = getString(R.string.guest_user)
            authBtn.setOnClickListener{
                startActivity(Intent(requireContext(),AuthActivity::class.java))
            }
        }
    }
}