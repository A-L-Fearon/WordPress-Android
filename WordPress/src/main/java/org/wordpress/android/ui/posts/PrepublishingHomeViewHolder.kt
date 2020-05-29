package org.wordpress.android.ui.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.wordpress.android.R
import org.wordpress.android.ui.posts.PrepublishingHomeItemUiState.SubmitButtonUiState
import org.wordpress.android.ui.posts.PrepublishingHomeItemUiState.HeaderUiState
import org.wordpress.android.ui.posts.PrepublishingHomeItemUiState.HomeUiState
import org.wordpress.android.ui.utils.UiHelpers
import org.wordpress.android.util.image.ImageManager
import org.wordpress.android.util.image.ImageType.BLAVATAR

sealed class PrepublishingHomeViewHolder(
    internal val parent: ViewGroup,
    @LayoutRes layout: Int
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false)) {
    abstract fun onBind(uiState: PrepublishingHomeItemUiState)

    class PrepublishingHomeListItemViewHolder(parentView: ViewGroup, val uiHelpers: UiHelpers) :
            PrepublishingHomeViewHolder(parentView, R.layout.prepublishing_action_list_item) {
        private val actionType: TextView = itemView.findViewById(R.id.action_type)
        private val actionResult: TextView = itemView.findViewById(R.id.action_result)
        private val actionLayout: View = itemView.findViewById(R.id.action_layout)

        override fun onBind(uiState: PrepublishingHomeItemUiState) {
            uiState as HomeUiState

            actionType.text = uiHelpers.getTextOfUiString(itemView.context, uiState.actionType.textRes)
            uiState.actionResult?.let { resultText ->
                actionResult.text = uiHelpers.getTextOfUiString(itemView.context, resultText)
            }

            if (uiState.actionEnabled) {
                actionLayout.setOnClickListener {
                    uiState.onActionClicked.invoke(uiState.actionType)
                }
            } else {
                actionType.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray_20))
                actionResult.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray_20))
            }
        }
    }

    class PrepublishingHeaderListItemViewHolder(
        parentView: ViewGroup,
        val uiHelpers: UiHelpers,
        val imageManager: ImageManager
    ) : PrepublishingHomeViewHolder(parentView, R.layout.prepublishing_home_header_list_item) {
        private val siteName: TextView = itemView.findViewById(R.id.site_name)
        private val siteIcon: ImageView = itemView.findViewById(R.id.site_icon)

        override fun onBind(uiState: PrepublishingHomeItemUiState) {
            uiState as HeaderUiState

            siteName.text = uiHelpers.getTextOfUiString(itemView.context, uiState.siteName)

            imageManager.load(siteIcon, BLAVATAR, uiState.siteIconUrl)
        }
    }

    class PrepublishingSubmitButtonViewHolder(parentView: ViewGroup, val uiHelpers: UiHelpers) :
            PrepublishingHomeViewHolder(parentView, R.layout.prepublishing_home_publish_button_list_item) {
        private val button: Button = itemView.findViewById(R.id.publish_button)

        override fun onBind(uiState: PrepublishingHomeItemUiState) {
            uiState as SubmitButtonUiState

            button.text = uiHelpers.getTextOfUiString(itemView.context, uiState.buttonText)
            button.setOnClickListener {
                uiState.onButtonClicked.invoke(uiState.publishPost)
            }
        }
    }
}