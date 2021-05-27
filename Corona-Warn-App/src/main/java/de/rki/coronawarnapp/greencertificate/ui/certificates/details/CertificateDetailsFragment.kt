package de.rki.coronawarnapp.greencertificate.ui.certificates.details

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import de.rki.coronawarnapp.R
import de.rki.coronawarnapp.databinding.FragmentVaccinationDetailsBinding
import de.rki.coronawarnapp.ui.view.onOffsetChange
import de.rki.coronawarnapp.util.di.AutoInject
import de.rki.coronawarnapp.util.ui.popBackStack
import de.rki.coronawarnapp.util.ui.viewBinding
import de.rki.coronawarnapp.util.viewmodel.CWAViewModelFactoryProvider
import de.rki.coronawarnapp.util.viewmodel.cwaViewModels
import org.joda.time.format.DateTimeFormat
import javax.inject.Inject

class CertificateDetailsFragment : Fragment(R.layout.fragment_greencertificate_details), AutoInject {

    @Inject lateinit var viewModelFactory: CWAViewModelFactoryProvider.Factory

    //private val args by navArgs<DetailsFra>()
    private val binding: FragmentVaccinationDetailsBinding by viewBinding()

    private val viewModel: CertificateDetailsViewModel by cwaViewModels { viewModelFactory }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) =
        with(binding) {
            toolbar.setNavigationOnClickListener { popBackStack() }

            appBarLayout.onOffsetChange { titleAlpha, subtitleAlpha ->
                title.alpha = titleAlpha
                subtitle.alpha = subtitleAlpha
            }
            setToolbarOverlay()

            viewModel.qrCode.observe(viewLifecycleOwner) {
                qrCodeCard.image.setImageBitmap(it)
                it?.let {
                    qrCodeCard.image.setOnClickListener { viewModel.openFullScreen() }
                    qrCodeCard.progressBar.hide()
                }
            }
        }

    private fun setToolbarOverlay() {
        val width = requireContext().resources.displayMetrics.widthPixels

        val params: CoordinatorLayout.LayoutParams = binding.scrollView.layoutParams
            as (CoordinatorLayout.LayoutParams)

        val textParams = binding.subtitle.layoutParams as (LinearLayout.LayoutParams)
        textParams.bottomMargin = (width / 3) - 24 /* 24 is space between screen border and QrCode */
        binding.subtitle.requestLayout() /* 24 is space between screen border and QrCode */

        val behavior: AppBarLayout.ScrollingViewBehavior = params.behavior as (AppBarLayout.ScrollingViewBehavior)
        behavior.overlayTop = (width / 3) - 24
    }

    companion object {
        private val format = DateTimeFormat.forPattern("dd.MM.yy")
    }
}
