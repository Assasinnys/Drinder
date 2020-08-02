package by.hackathon.drinder.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import by.hackathon.drinder.R
import by.hackathon.drinder.util.mainActivity

class UserDetailEditFragment : Fragment(R.layout.fragment_user_detail_edit) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        mainActivity().setActionBarTitle(R.string.title_user_details_edit)
    }
}