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
    window.documentPage = window.documentPage || {
        closeAllTranslationForms: () => util.findAllByData({ key: 'is-translation-form-container', value: 'true' }).forEach(formContainer => util.removeInnerHTML(formContainer)),
        cleanupSelectedWord: () => {
            const familiarityColors = new Map([
                ['unknown', ['bg-accent', 'text-primary']],
                ['recognized', ['bg-accent/80', 'text-primary']],
                ['familiar', ['bg-accent/60', 'text-primary']],
                ['known', ['bg-primary', 'text-neutral']],
                ['ignored', ['bg-primary', 'text-neutral']]
            ])   
            util.findAllByData({ key: 'is-word', value: 'true' })
            .filter(word => data.get(word, 'is-selected') === 'true')
            .flatMap(selectedWord => [...selectedWord.children])
            .filter(child => child instanceof HTMLSpanElement)
            .forEach(selectedSpan => {
                data.set(selectedSpan.parentElement, { key: 'is-selected', value: 'false' })
                util.replaceClasses(selectedSpan, {
                    toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-accent', 'bg-tertiary'],
                    toAdd: familiarityColors.get(data.get(selectedSpan.parentElement, 'familiarity'))
                })
            })
        },
        cleanupSelectedPhrase: () => {    
            const familiarityColors = new Map([
                ['unknown', ['bg-accent', 'text-primary']],
                ['recognized', ['bg-accent/80', 'text-primary']],
                ['familiar', ['bg-accent/60', 'text-primary']],
                ['known', ['bg-primary', 'text-neutral']],
                ['ignored', ['bg-primary', 'text-neutral']]
            ])           
            util.findAllByData({ key: 'is-phrase', value: 'true' })
            .filter(node => data.get(node, 'is-selected') === 'true')
            .forEach(selectedPhrase => {
                data.set(selectedPhrase, { key: 'is-selected', value: 'false' })

                if(data.get(selectedPhrase, 'is-saved') === 'true') {
                    util.replaceClasses(selectedPhrase, {
                        toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-accent', 'bg-tertiary'],
                        toAdd: [...familiarityColors.get(data.get(selectedPhrase, 'familiarity'))]
                    })
                    const words = Array.from(selectedPhrase.children)
                    .filter(child => data.get(child, 'is-word') === 'true')
                    words.forEach(word => word.classList.add('pointer-events-none'))

                    const spans = words
                    .flatMap(word => [...word.children])
                    .filter(word => word instanceof HTMLSpanElement)
                    spans.forEach(span => {
                        util.replaceClasses(span, {
                            toRemove: ['text-accent', 'text-primary'],
                            toAdd: [familiarityColors.get(data.get(selectedPhrase, 'familiarity')).at(1)]
                        })
                    })
                }
                else {
                    selectedPhrase.firstElementChild.remove()
                    const phraseNodes = util.unwrap(selectedPhrase).filter(node => data.get(node, 'is-word'))
                    phraseNodes.forEach(node => {
                        htmx.process(node)
                        util.replaceClasses(node, {
                            toRemove: ['pointer-events-none'],
                            toAdd: ['border', 'border-2', 'border-transparent']
                        })
                        const span = [...node.children].filter(child => child instanceof HTMLSpanElement).at(0)
                        util.replaceClasses(span, {
                            toRemove: ['bg-accent', 'bg-accent/80', 'bg-accent/60', 'bg-accent/40', 'bg-primary', 'text-primary', 'text-accent', 'bg-tertiary', 'bg-tertiary'],
                            toAdd: familiarityColors.get(data.get(node, 'familiarity'))
                        })
                    })
                }
            })
        }
    }
