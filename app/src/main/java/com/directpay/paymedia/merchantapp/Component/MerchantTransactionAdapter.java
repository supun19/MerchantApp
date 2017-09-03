package com.directpay.paymedia.merchantapp.Component;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;


import com.directpay.paymedia.merchantapp.LoginActivity;
import com.directpay.paymedia.merchantapp.Model.MerchantTransactionModel;
import com.directpay.paymedia.merchantapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by supun on 09/07/17.
 */

/**
 * Created by supun on 30/05/17.
 */



public class MerchantTransactionAdapter extends ArrayAdapter<MerchantTransactionModel> {

    private int size;
    private List<MerchantTransactionModel>originalData = null;
    private List<MerchantTransactionModel>filteredData = null;
    private LayoutInflater mInflater;
    private ItemFilter mFilter = new ItemFilter();

    public MerchantTransactionAdapter(Context context, ArrayList<MerchantTransactionModel> transactions,int size) {
        super(context, 0, transactions);
        this.size = size;
        this.filteredData = transactions ;
        this.originalData = transactions ;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final MerchantTransactionModel transactionModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.merchant_transaction_item, parent, false);
        }
        // Lookup view for data population
        TextView AccountNumber = (TextView) convertView.findViewById(R.id.userAccountNumber);
        TextView Amount = (TextView) convertView.findViewById(R.id.amount);
        TextView Date = (TextView) convertView.findViewById(R.id.date);
        AccountNumber.setWidth(size);
        Amount.setWidth(size);
        Date.setWidth(size);

        // Populate the data into the template view using the data objec
//        if(transactionModel.getToAccountRole().equals("merchant")){
//            AccountNumber.setText(transactionModel.getFromAccountNumber());
//        }
//        else if(transactionModel.getToAccountRole().equals("merchant")){
//            AccountNumber.setText(transactionModel.getFromAccountNumber());
//        }



        AccountNumber.setText(transactionModel.getUser().getLastName());

        //Log.d("lastNAme",transactionModel.getUser().getLastName());

        Amount.setText(transactionModel.getAmount()+" LKR");
        Date.setText(transactionModel.getDate());
        // Return the completed view to render on screen


        return convertView;
    }


    public void moveLogin(){

        Intent myIntent = new Intent(getContext(), LoginActivity.class);
        getContext().startActivity(myIntent);

    }

    public int getCount() {
        return filteredData.size();
    }

    public MerchantTransactionModel getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString();

            FilterResults results = new FilterResults();

            final List<MerchantTransactionModel> list = originalData;

            int count = list.size();
            final ArrayList<MerchantTransactionModel> nlist = new ArrayList<MerchantTransactionModel>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                MerchantTransactionModel model = list.get(i);
                filterableString = list.get(i).getUser().getLastName();
//                if(model.isAppUserAccount_isFromAccountNuber()){
//                    filterableString = list.get(i).getToAccountNumber();
//                }
//                else {
//                    filterableString = list.get(i).getFromAccountNumber();
//                }

                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(model);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<MerchantTransactionModel>) results.values;
            notifyDataSetChanged();
        }

    }
}



