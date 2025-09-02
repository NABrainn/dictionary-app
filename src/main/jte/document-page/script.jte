@param int selectedWordId
@param int documentId
@raw
<script class="
top-8 top-6 top-10 right-4 bottom-12 bottom-10 -right-16 top-4 top-16 bottom-16 bottom-8 left-2 right-2 -left-16 -left-48
border-b-secondary border-l-secondary border-t-secondary border-r-secondary border-l-transparent border-r-transparent border
border-l-2 border-r-2 border-t-2 border-b-2
px-0.5 pl-0.5 pr-0.5 w-[2px] min-w-[2px]">
    //ABOVE styles used here solely for tailwind live reload server to pick up

    if (!customElements.get('form-position-adjuster')) {
        class FormPositionAdjuster extends HTMLElement {

            static observedAttributes = ['selected-word-id'];
            debug = true

            positions = {
                left: 'left-2',
                right: '',
                top: 'top-10',
                bottom: ''
            }

            constructor() {
                super();
            }

            log() {
                this.debug = true
            }
            updatePositions(id) {                
                const form = this.#getTranslationForm(id);         
                this.#handleOverflow(form);
            }
            updateFormPosition(form) {
                this.#adjustPosition({
                    form: form,
                    left: 2,
                    top: 10
                })
                this.#handleOverflow(form);
            }
            #updateFormData() {
                const wordId = this.getAttribute('selected-word-id')
                htmx.findAll('.left-' + wordId).forEach((input) => {
                    input.value = this.positions.left
                })
                htmx.findAll('.right-' + wordId).forEach((input) => {
                    input.value = this.positions.right
                })
                htmx.findAll('.top-' + wordId).forEach((input) => {
                    input.value = this.positions.top
                })
                htmx.findAll('.bottom-' + wordId).forEach((input) => {
                    input.value = this.positions.bottom
                })
            }
            #getTranslationForm(id) {
                return htmx.find('#translation-form-container-' +  id);
            }
            #handleOverflow(form) {
                let neOverflow, nwOverflow, seOverflow, swOverflow, sOverflow, wOverflow;
                // do {
                //     neOverflow = this.#handleNorthEastOverflow(form);
                //     nwOverflow = this.#handleNorthWestOverflow(form);
                //     seOverflow = this.#handleSouthEastOverflow(form);
                //     swOverflow = this.#handleSouthWestOverflow(form);
                //     sOverflow = this.#handleSouthOverflow(form);
                // } while ([neOverflow, nwOverflow, seOverflow, swOverflow, sOverflow].some(Boolean));

                for(let i = 0; i < 30; i++) {
                    neOverflow = this.#handleNorthEastOverflow(form);
                    nwOverflow = this.#handleNorthWestOverflow(form);
                    seOverflow = this.#handleSouthEastOverflow(form);
                    swOverflow = this.#handleSouthWestOverflow(form);
                    sOverflow = this.#handleSouthOverflow(form);
                    wOverflow = this.#handleWestOverflow(form)
                }
            }

            #handleNorthEastOverflow(form) {
                if(!form) {
                    return
                }
                if (this.#isOverflowRight(form) && !this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        top: 0,
                        bottom: 0,
                        left: 0,
                        right: 0
                    });
                    if(this.debug) {
                        console.log('overflow NE')
                    }
                    return true
                }
                return false

            }
            #handleNorthWestOverflow(form) {
                if(!form) {
                    return
                }
                if (this.#isOverflowLeft(form) && !this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        top: 0,
                        bottom: 0,
                        left: 0,
                        right: 0
                    });
                    if(this.debug) {
                        console.log('overflow NW')
                    }
                    return true
                }
                return false
            }
            #handleSouthEastOverflow(form) {
                if(!form) {
                    return
                }
                if (this.#isOverflowRight(form) && this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        top: 0,
                        bottom: 8,
                        left: -48,
                        right: 0
                    });
                    if(this.debug) {
                        console.log('overflow SE')
                    }
                    return true
                }
                return false
            }
            #handleSouthWestOverflow(form) {
                if(!form) {
                    return
                }
                if (this.#isOverflowLeft(form) && this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        top: 0,
                        bottom: 0,
                        left: 0,
                        right: 0
                    });
                    if(this.debug) {
                        console.log('overflow SW')
                    }
                    return true
                }
                return false
            }

            #handleSouthOverflow(form) {
                if(!form) {
                    return
                }
                if (!this.#isOverflowLeft(form) && !this.#isOverflowRight(form) && this.#isOverflowBottom(form)) {
                    this.#adjustPosition({
                        form: form,
                        top: 0,
                        bottom: 0,
                        left: 0,
                        right: 0
                    });
                    if(this.debug) {
                        console.log('overflow S')
                    }
                    return true
                }
                return false
            }

            #handleWestOverflow(form) {
                if(!form) {
                    return
                }
                if(this.#isOverflowLeft(form)) {
                    this.#adjustPosition({
                        form: form,
                        left: 0,
                        right: 0
                    });
                    if(this.debug) {
                        console.log('overflow W')
                    }
                    return true
                }
                return false
            }

            #isOverflowLeft(form) {
                if(!form) {
                    return
                }
                return form.getBoundingClientRect().left < 0;
            }

            #isOverflowRight(form) {
                if(!form) {
                    return
                }
                return form.getBoundingClientRect().right > window.innerWidth;
            }

            #isOverflowBottom(form) {   
                if(!form) {
                    return
                }           
                return form.getBoundingClientRect().bottom > window.innerHeight;
            }

            #adjustPosition(config) {
                this.#cleanupPositions(config)
                const rightPos = this.#assignRightPosition(config.right);
                const leftPos = this.#assignLeftPosition(config.left);
                const bottomPos = this.#assignBottomPosition(config.bottom);
                const topPos = this.#assignTopPosition(config.top);

                const positions = {}
                if(leftPos) positions.left = leftPos;
                if(rightPos) positions.right = rightPos;
                if(bottomPos) positions.top = topPos;
                if(topPos) positions.bottom = bottomPos;

                for(const [key, position] of Object.entries(positions)) {
                    if(position !== '') {
                        this.#updateClassList(config.form, position)
                    }
                }
                this.positions = positions
                if(this.debug) {
                    console.log('positions updated: ', this.positions)
                }
            }

            #updateClassList(form, value) {
                if(!form) {
                    return
                }
                form.classList.add(value)
                if(this.debug) {
                    console.log('classList updated: ', form.classList)
                }
            }

            #assignRightPosition(value) {
                if(value !== undefined) {
                    if(value >= 0) {
                        return "right-" + value;
                    }
                    return "-right-" + Math.abs(value)
                }
            }

            #assignLeftPosition(value) {
                if(value !== undefined) {
                    if(value >= 0) {
                        return "left-" + value;
                    }
                    return "-left-" + Math.abs(value)
                }
            }

            #assignBottomPosition(value) {
                if(value !== undefined) {
                    if(value >= 0) {
                        return "bottom-" + value;
                    }
                    return "-bottom-" + Math.abs(value)
                }
            }

            #assignTopPosition(value) {
                if(value !== undefined) {
                    if(value >= 0) {
                        return "top-" + value;
                    }
                    return "-top-" + Math.abs(value)
                }
            }

            #cleanupPositions(config) {
                if(!config) {
                    return
                }
                if(!config.form) {
                    return
                }
                const positionClasses = Array.from(config.form.classList).filter(cls =>
                    cls.match(/^(right-|left-|top-|bottom-|-right-|-left-|-top-|-bottom-)\d+$/)
                );
                positionClasses.forEach(cls => config.form.classList.remove(cls));
                if(this.debug) {
                    console.log('positions cleaned up: ', positionClasses)
                }
            }
        }

        customElements.define('form-position-adjuster', FormPositionAdjuster);
    }
@endraw
</script>
<form-position-adjuster selected-word-id="0"></form-position-adjuster>




