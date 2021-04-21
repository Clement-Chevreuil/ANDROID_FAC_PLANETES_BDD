package com.example.bdd.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bdd.R;

public class SimpleDialogFragment extends DialogFragment {


    private EditText mEditTextNom, mEditTextTaille;
    private com.example.bdd.dialog.SimpleDialogListener listener;
    private Button btn;


    public SimpleDialogFragment() {
        // le fragment est créé par la méthode newInstance
    }

    public static com.example.bdd.dialog.SimpleDialogFragment newInstance(String title) {

        com.example.bdd.dialog.SimpleDialogFragment frag = new com.example.bdd.dialog.SimpleDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_simple_dialog, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        listener = (com.example.bdd.dialog.SimpleDialogListener) getActivity();

        mEditTextNom = (EditText) view.findViewById(R.id.txt_nom_planete);
        mEditTextTaille = (EditText) view.findViewById(R.id.txt_taille_planete);

        btn = (Button) view.findViewById(R.id.ok);

        // quand le button est cliqué, l'activité est appellé,
        // la valeur mEditText est passeé à l'activité en paramètre

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOkClickDialog(mEditTextNom.getText().toString(), mEditTextNom.getText().toString() );
                dismiss();
            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


    }


}
