package uz.gita.mycontactbyretrofit.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.gita.mycontactbyretrofit.R
import uz.gita.mycontactbyretrofit.data.model.ContactUIData
import uz.gita.mycontactbyretrofit.data.model.StatusEnum
import uz.gita.mycontactbyretrofit.databinding.ItemContactBinding

class ContactAdapter :
    ListAdapter<ContactUIData, ContactAdapter.ContactViewHolder>(ContactDiffUtil) {
    var clickListener: ((data: ContactUIData) -> Unit)? = null

    object ContactDiffUtil : DiffUtil.ItemCallback<ContactUIData>() {
        override fun areItemsTheSame(oldItem: ContactUIData, newItem: ContactUIData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ContactUIData,
            newItem: ContactUIData
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class ContactViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            getItem(adapterPosition).apply {
                binding.textName.text = "$lastName $firstName"
                binding.textNumber.text = phone

                when(this.status) {
                    StatusEnum.SYNC -> binding.textStatus.visibility = View.GONE
                    StatusEnum.ADD -> {
                        binding.textStatus.visibility = View.VISIBLE
                        binding.textStatus.text = "Add"
                        binding.textStatus.setBackgroundResource(R.drawable.bg_status_add)
                    }
                    StatusEnum.EDIT -> {
                        binding.textStatus.visibility = View.VISIBLE
                        binding.textStatus.text = "Edit"
                        binding.textStatus.setBackgroundResource(R.drawable.bg_status_edit)
                    }
                    else -> {
                        binding.textStatus.visibility = View.VISIBLE
                        binding.textStatus.text = "Del"
                        binding.textStatus.setBackgroundResource(R.drawable.bg_status_delete)
                    }
                }
                binding.buttonMore.setOnClickListener{
                    clickListener?.invoke(this)
                }
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