package eatyourbeets.cards.animator;

import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.interfaces.metadata.Hidden;

public abstract class MelzalgaldAlt extends AnimatorCard implements Hidden
{
    public static final String ID = Register(MelzalgaldAlt.class.getSimpleName());

    public MelzalgaldAlt(String id)
    {
        super(staticCardData.get(id), id, Resources_Animator.GetCardImage(id), 1, CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF_AND_ENEMY);

        this.exhaust = true;
    }
}