import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.example.myapplication.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet.editButton
import kotlinx.android.synthetic.main.bottom_sheet.linkButton
import kotlinx.android.synthetic.main.bottom_sheet.shareButton

import com.budiyev.android.codescanner.*
class bottom_sheet :  BottomSheetDialogFragment() {
    private lateinit var codeScanner: CodeScanner
    var listener: OnDialogButtonFragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        shareButton.setOnClickListener {
            listener?.onSelectDialog("Share")
            dismiss()
        }

        linkButton.setOnClickListener {
            listener?.onSelectDialog("Link")
            dismiss()
        }

        editButton.setOnClickListener {
            listener?.onSelectDialog("Edit")
            dismiss()
        }

    }

    interface OnDialogButtonFragmentListener {

        fun onSelectDialog(select: String)
    }
}
