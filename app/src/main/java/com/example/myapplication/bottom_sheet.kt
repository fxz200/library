import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.budiyev.android.codescanner.CodeScanner
import com.example.myapplication.GlobalVariable
import com.example.myapplication.R
import com.example.myapplication.cam2
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.bottom_sheet.box1_1
import kotlinx.android.synthetic.main.bottom_sheet.box1_2
import kotlinx.android.synthetic.main.bottom_sheet.box1_3
import kotlinx.android.synthetic.main.bottom_sheet.box1_4
import kotlinx.android.synthetic.main.bottom_sheet.editButton
import kotlinx.android.synthetic.main.bottom_sheet.linkButton
import kotlinx.android.synthetic.main.bottom_sheet.shareButton


class bottom_sheet :  BottomSheetDialogFragment() {
    private lateinit var codeScanner: CodeScanner
    var listener: OnDialogButtonFragmentListener? = null
    var a1trigger = arguments?.getString("a1trigger")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.AppBottomSheet)


    }

    override fun onCreateView(


        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        return inflater.inflate(R.layout.bottom_sheet, container, false)
        val check1= view?.findViewById<CheckBox>(R.id.box1_1)
        val check2= view?.findViewById<CheckBox>(R.id.box1_2)
        val check3= view?.findViewById<CheckBox>(R.id.box1_3)
        val check4= view?.findViewById<CheckBox>(R.id.box1_4)
        val bundle = arguments
        val mess = bundle?.getString("data")



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var a1trigger=GlobalVariable.geta1trigger()
        var a2trigger=GlobalVariable.geta2trigger()
        var a3trigger=GlobalVariable.geta3trigger()
        var a4trigger=GlobalVariable.geta4trigger()
        var b1trigger=GlobalVariable.getb1trigger()
        var b2trigger=GlobalVariable.getb2trigger()
        var b3trigger=GlobalVariable.getb3trigger()




        if(a1trigger=="1"){
            box1_1.isChecked=true
        }
        if(a2trigger=="1"){
            box1_2.isChecked=true
        }
        if(a3trigger=="1"){
            box1_3.isChecked=true
        }
        if(a4trigger=="1"){
            box1_4.isChecked=true
        }

        shareButton.setOnClickListener {
            listener?.onSelectDialog("Share")
            dismiss()
        }

        linkButton.setOnClickListener {
            listener?.onSelectDialog("Link")
            dismiss()
        }

        editButton.setOnClickListener {
            GlobalVariable.seta1trigger("0")
            GlobalVariable.seta2trigger("0")
            GlobalVariable.seta3trigger("0")
            GlobalVariable.seta4trigger("0")
            GlobalVariable.setb1trigger("0")
            GlobalVariable.setb2trigger("0")
            GlobalVariable.setb3trigger("0")
            listener?.onSelectDialog("Edit")
            dismiss()
        }

    }

    interface OnDialogButtonFragmentListener {

        fun onSelectDialog(select: String)
    }
}
