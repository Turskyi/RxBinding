package io.github.turskyi.rxbinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.github.turskyi.rxbinding.databinding.ActivityMainBinding
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // old way
//        editText.addTextChangedListener {
//            textView.text = it
//        }

        // coping the text from edit text according to provided logic
//        val disposable = RxTextView.textChanges(binding.editText)
//            .filter {
//                it.length > 2
//            }
//            .debounce(100, TimeUnit.MILLISECONDS)
//            .subscribe {
//                binding.textView.text = "$it"
//            }

        /**
         * shows in text view a number of characters in edit text
         */
        val disposable3 = RxTextView.textChanges(binding.editText)
                .filter {
                    it.length > 2
                }
                .debounce(100, TimeUnit.MILLISECONDS)
                .switchMap {
                    // TODO: call API or DB
                    getModifiedObservable(it.length)
                }
                .subscribe {
                    binding.textView.text = getString(R.string.length, it)
                }


        //  clearing the edit text on click
        val disposable2 = RxView.clicks(binding.btnClear)
                .subscribe {
                    binding.editText.setText("")
                    binding.textView.text = ""
                }

    }

    /**
     * emitter is the one who works in background
     */
    private fun getModifiedObservable(integer: Int): io.reactivex.Observable<Int> {
        return io.reactivex.Observable.create(ObservableOnSubscribe<Int> { emitter ->
            emitter.onNext(integer)
            emitter.onComplete()
        }).subscribeOn(Schedulers.io())
    }
}
