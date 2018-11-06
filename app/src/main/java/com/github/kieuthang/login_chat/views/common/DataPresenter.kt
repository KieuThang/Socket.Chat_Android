package com.github.kieuthang.login_chat.views.common


import android.content.Context
import com.github.kieuthang.login_chat.data.DataRepositoryImpl
import com.github.kieuthang.login_chat.data.DefaultSubscriber
import com.github.kieuthang.login_chat.data.entity.AccessToken
import com.github.kieuthang.login_chat.data.entity.UserModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DataPresenter internal constructor(context: Context) : BaseContract.Presenter<IDataLoadView> {
    private var iLoginDataLoadView: IDataLoadView? = null
    private var iDataRepository: IDataRepository? = DataRepositoryImpl(context)


    override fun bindView(view: IDataLoadView) {
        iLoginDataLoadView = view
    }

    override fun release() {

    }

    fun login(email: String, password: String) {
        iLoginDataLoadView!!.showLoading()
        iDataRepository!!.login(email, password).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DefaultSubscriber<AccessToken>() {
                    override fun onNext(t: AccessToken) {
                        super.onNext(t)
                        iLoginDataLoadView!!.hideLoading()
                        iLoginDataLoadView!!.onLoginResult(t)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        iLoginDataLoadView!!.hideLoading()
                        iLoginDataLoadView!!.onLoginResult(null)
                    }
                })
    }

    internal fun getMyProfile(isPullToRefresh: Boolean) {
        iDataRepository!!.getMyProfile(isPullToRefresh).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DefaultSubscriber<UserModel>() {
                    override fun onNext(t: UserModel) {
                        super.onNext(t)
                        iLoginDataLoadView!!.hideLoading()
                        iLoginDataLoadView!!.onGetMyProfileResult(t, null)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        iLoginDataLoadView!!.hideLoading()
                        iLoginDataLoadView!!.onGetMyProfileResult(null, e)
                    }
                })
    }

    fun register(firstName: String, lastName: String, email: String, password: String) {
        iDataRepository!!.register(firstName, lastName, email, password).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DefaultSubscriber<AccessToken>() {
                    override fun onNext(t: AccessToken) {
                        super.onNext(t)
                        iLoginDataLoadView!!.hideLoading()
                        iLoginDataLoadView!!.onRegisterResult(t, null)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        iLoginDataLoadView!!.hideLoading()
                        iLoginDataLoadView!!.onRegisterResult(null, e)
                    }
                })
    }
}
