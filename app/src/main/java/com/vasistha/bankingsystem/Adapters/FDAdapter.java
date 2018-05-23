package com.vasistha.bankingsystem.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vasistha.bankingsystem.ModelClasses.FixedDepositData;
import com.vasistha.bankingsystem.R;

import java.util.List;

/**
 * Created by Ashutosh Singh on 09-Mar-18.
 */

public class FDAdapter extends ArrayAdapter<FixedDepositData>
{
    private Context context;
    private List<FixedDepositData> fdList;

    public FDAdapter(@NonNull Context context, List<FixedDepositData> fdList)
    {
        super(context, R.layout.fd_list,fdList);
        this.context = context;
        this.fdList = fdList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View fdView = convertView;

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        fdView = layoutInflater.inflate(R.layout.fd_list,null,true);

        TextView startDate = fdView.findViewById(R.id.fdStartDate);
        TextView fixedAmount = fdView.findViewById(R.id.fixedAmount);
        TextView endDate = fdView.findViewById(R.id.endDate);
        TextView maturityAmount = fdView.findViewById(R.id.maturityAmount);

        FixedDepositData fixedDepositData = fdList.get(position);

        startDate.setText(fixedDepositData.getStartDate());
        fixedAmount.setText(Integer.toString(fixedDepositData.getFixedDepositAmount()));
        endDate.setText(fixedDepositData.getEndDate());
        maturityAmount.setText(Integer.toString(fixedDepositData.getMaturityAmount()));

        return fdView;
    }
}
