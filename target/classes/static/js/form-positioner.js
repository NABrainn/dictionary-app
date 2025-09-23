window.formPositioner = window.formPositioner || {
    debug: false,
    handleOverflow(form, sourceWordQuantity) {
        if(!form) {
            return
        }
        const height = window.innerHeight;
        const width = window.innerWidth
        //reset position
        form.style.top = '32px'
        form.style.left = ''
        form.style.right = ''
        form.style.bottom = ''
        if(this.debug) {
            console.log('running checks...')
        }
        //handle bottom overflow
        if(form.getBoundingClientRect().bottom > height) {
            if(this.debug) {
                console.log('overflow bottom', form.getBoundingClientRect())
            }
            form.style.top = ''
            form.style.bottom = '32px'
            if(this.debug) {
                console.log('bottom', form.style.bottom)
            }
        }
        //handle right overflow
        if(form.getBoundingClientRect().right > width) {
            if(this.debug) {
                console.log('overflow right', form.getBoundingClientRect())
            }
            form.style.right = '8px'
            if(this.debug) {
                console.log('right', form.style.right)
            }
        }

        //handle left underflow
        if(form.getBoundingClientRect().left < 0) {
            if(this.debug) {
                console.log('overflow left', form.getBoundingClientRect())
            }
            form.style.right = '-132px'
            if(this.debug) {
                console.log('left', form.style.left)
            }
        }
        if(this.debug) {
            console.log('finally', form.style.left, form.style.right, form.style.top, form.style.bottom)
        }
    }
}