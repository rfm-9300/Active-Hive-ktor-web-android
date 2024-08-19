package rfm.biblequizz.data.log

import org.jetbrains.annotations.NonNls

class Timber {
    companion object {
        /** Registers a user in the log */


        /** Log an info message with optional format args.  */
        fun i(@NonNls message: String?) {
            timber.log.Timber.i(message)
        }

        /** Log an info message with optional format args.  */
        fun i(@NonNls message: String?, vararg args: Any?) {
            timber.log.Timber.i(message, *args)
        }

        /** Log an info exception and a message with optional format args.  */
        fun i(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
            timber.log.Timber.i(t, message, *args)
        }

        /** Log an info exception.  */
        fun i(t: Throwable?) {
            timber.log.Timber.i(t)
        }

        /** Log a warning message with optional format args.  */
        fun w(@NonNls message: String?, vararg args: Any?) {
            timber.log.Timber.w(message, *args)
        }

        /** Log a warning exception and a message with optional format args.  */
        fun w(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
            timber.log.Timber.w(t, message, *args)
        }

        /** Log a warning exception.  */
        fun w(t: Throwable?) {
            timber.log.Timber.w(t)
        }

        /** Log an error message with optional format args.  */
        fun e(@NonNls message: String?, vararg args: Any?) {
            timber.log.Timber.e(message, *args)
        }

        /** Log an error exception and a message with optional format args.  */
        fun e(t: Throwable?, @NonNls message: String?, vararg args: Any?) {
            timber.log.Timber.e(t, message, *args)
        }

        /** Log an error exception.  */
        fun e(t: Throwable?) {
            timber.log.Timber.e(t)
        }
    }
}