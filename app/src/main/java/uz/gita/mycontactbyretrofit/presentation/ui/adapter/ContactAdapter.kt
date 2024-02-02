package uz.gita.mycontactbyretrofit.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.mycontactbyretrofit.data.remote.response.ContactResponse
import uz.gita.mycontactbyretrofit.databinding.ItemContactBinding

class ContactAdapter :
    ListAdapter<ContactResponse, ContactAdapter.ContactViewHolder>(ContactDiffUtil) {
    var clickListener: ((data: ContactResponse) -> Unit)? = null

    object ContactDiffUtil : DiffUtil.ItemCallback<ContactResponse>() {
        override fun areItemsTheSame(oldItem: ContactResponse, newItem: ContactResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ContactResponse,
            newItem: ContactResponse
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class ContactViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            getItem(absoluteAdapterPosition).apply {
                binding.buttonMore.setOnClickListener {
                    clickListener?.invoke(this)
                }
                binding.textName.text = "$lastName $firstName"
                binding.textNumber.text = phone
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ContactViewHolder(
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind()
    }
}